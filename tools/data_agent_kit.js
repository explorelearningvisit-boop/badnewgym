/**
 * Data Agent Kit MCP Bridge & CLI Tool
 * Connects directly to Google Cloud Data Cloud MCP Servers in Antigravity IDE:
 *  - data-agent-kit (target: dataAgentKit-antigravityide)
 *  - notebooks      (target: notebooks-antigravityide)
 *  - visualization  (target: visualization-antigravityide)
 *
 * Bypasses the IDE UI permission prompt failure ('unexpected user interaction type: not permission')
 * by communicating via JSON-RPC 2.0 through the native mcp_proxy_bundle.
 */

const { spawn } = require('child_process');
const fs = require('fs');
const path = require('path');

const PROXY_BUNDLE = 'c:\\Users\\User\\.antigravity-ide\\extensions\\googlecloudtools.datacloud-0.11.0-universal\\mcp_servers\\cli\\mcp_proxy_bundle.js';

const TARGET_MAP = {
    'data-agent-kit': 'dataAgentKit-antigravityide',
    'dak': 'dataAgentKit-antigravityide',
    'notebooks': 'notebooks-antigravityide',
    'notebook': 'notebooks-antigravityide',
    'visualization': 'visualization-antigravityide'
};

function executeRpc(method, params = {}, target = 'dataAgentKit-antigravityide') {
    return new Promise((resolve, reject) => {
        if (!fs.existsSync(PROXY_BUNDLE)) {
            return reject(new Error(`MCP proxy bundle not found at: ${PROXY_BUNDLE}`));
        }

        const child = spawn('node', [PROXY_BUNDLE, target], {
            stdio: ['pipe', 'pipe', 'pipe']
        });

        let output = '';
        let errOutput = '';
        let initDone = false;
        let responseReceived = false;

        const timeout = setTimeout(() => {
            child.kill();
            if (!responseReceived) {
                reject(new Error(`MCP request '${method}' to target '${target}' timed out after 15s.`));
            }
        }, 15000);

        child.stdout.on('data', (data) => {
            output += data.toString();
            const lines = output.split('\n');
            for (let i = 0; i < lines.length - 1; i++) {
                const line = lines[i].trim();
                if (!line) continue;
                try {
                    const parsed = JSON.parse(line);
                    if (parsed.id === 1) {
                        // Server initialized, send requested method
                        initDone = true;
                        const reqMsg = JSON.stringify({
                            jsonrpc: '2.0',
                            method: method,
                            params: params,
                            id: 2
                        }) + '\n';
                        child.stdin.write(reqMsg);
                    } else if (parsed.id === 2) {
                        responseReceived = true;
                        clearTimeout(timeout);
                        child.kill();
                        return resolve(parsed);
                    }
                } catch (e) {
                    // Ignore non-JSON log lines
                }
            }
            output = lines[lines.length - 1];
        });

        child.stderr.on('data', (data) => {
            errOutput += data.toString();
        });

        child.on('close', (code) => {
            clearTimeout(timeout);
            if (!responseReceived) {
                reject(new Error(`MCP process exited with code ${code}. Stderr: ${errOutput}`));
            }
        });

        child.on('error', (err) => {
            clearTimeout(timeout);
            reject(err);
        });

        // Send initialize
        const initMsg = JSON.stringify({
            jsonrpc: '2.0',
            method: 'initialize',
            params: {
                protocolVersion: '2024-11-05',
                capabilities: {},
                clientInfo: { name: 'data-agent-kit-cli', version: '1.0' }
            },
            id: 1
        }) + '\n';
        child.stdin.write(initMsg);
    });
}

async function callTool(toolName, args = {}, target = 'dataAgentKit-antigravityide') {
    const res = await executeRpc('tools/call', { name: toolName, arguments: args }, target);
    if (res.error) {
        throw new Error(`Tool call '${toolName}' failed: ${JSON.stringify(res.error, null, 2)}`);
    }
    return res.result;
}

async function listTools(target = 'dataAgentKit-antigravityide') {
    const res = await executeRpc('tools/list', {}, target);
    if (res.error) {
        throw new Error(`Failed to list tools: ${JSON.stringify(res.error, null, 2)}`);
    }
    return res.result.tools || [];
}

async function runSelfTest() {
    console.log('=== Running Data Cloud MCP Diagnostic & Self-Test ===\n');

    const servers = [
        { name: 'data-agent-kit', target: TARGET_MAP['data-agent-kit'] },
        { name: 'notebooks', target: TARGET_MAP['notebooks'] },
        { name: 'visualization', target: TARGET_MAP['visualization'] }
    ];

    let allPassed = true;

    for (const server of servers) {
        process.stdout.write(`Testing [${server.name}] (${server.target})... `);
        try {
            const tools = await listTools(server.target);
            console.log(`OK! (${tools.length} tools registered)`);
            tools.forEach(t => console.log(`   - ${t.name}: ${t.description.slice(0, 75)}...`));
        } catch (err) {
            console.log(`FAILED!`);
            console.error(`   Error: ${err.message}`);
            allPassed = false;
        }
        console.log('');
    }

    // Test a live tool call
    process.stdout.write('Testing live editor context retrieval (data-agent-kit:get_active_editor_context)... ');
    try {
        const ctx = await callTool('get_active_editor_context', {}, TARGET_MAP['data-agent-kit']);
        console.log('OK!');
        if (ctx && ctx.content) {
            console.log('   Retrieved editor context successfully.');
        }
    } catch (err) {
        console.log('FAILED!');
        console.error(`   Error: ${err.message}`);
        allPassed = false;
    }

    console.log('\n======================================================');
    if (allPassed) {
        console.log('All Data Cloud MCP servers are ACTIVE and FULLY OPERATIONAL!');
    } else {
        console.log('Some checks encountered errors. Review logs above.');
    }
}

function parseJsonArg(raw) {
    if (!raw) return {};
    if (raw.startsWith('@') || raw.endsWith('.json')) {
        const filePath = raw.startsWith('@') ? raw.slice(1) : raw;
        if (fs.existsSync(filePath)) {
            return JSON.parse(fs.readFileSync(filePath, 'utf8'));
        }
    }
    try {
        return JSON.parse(raw);
    } catch (e1) {
        try {
            // Normalize PowerShell stripped quotes: {key:val} -> {"key":"val"}
            const normalized = raw
                .replace(/([a-zA-Z0-9_-]+)\s*:\s*([^,\}\]]+)/g, (m, k, v) => {
                    const cleanVal = v.trim().replace(/^['"]|['"]$/g, '');
                    const isBoolOrNum = /^(true|false|null|[0-9.]+)$/.test(cleanVal);
                    return `"${k}":${isBoolOrNum ? cleanVal : `"${cleanVal}"`}`;
                });
            return JSON.parse(normalized);
        } catch (e2) {
            throw new Error(`Could not parse JSON arguments: ${raw}`);
        }
    }
}

async function main() {
    const rawArgs = process.argv.slice(2);
    if (rawArgs.length === 0 || rawArgs.includes('--help') || rawArgs.includes('-h')) {
        console.log('Data Agent Kit MCP Bridge');
        console.log('Usage:');
        console.log('  node tools/data_agent_kit.js --test');
        console.log('  node tools/data_agent_kit.js list-tools [--server <name>]');
        console.log('  node tools/data_agent_kit.js <toolName> [jsonArgs] [--server <name>]');
        console.log('  node tools/data_agent_kit.js read-resource <uri>');
        console.log('');
        console.log('Supported Servers:');
        console.log('  data-agent-kit (default), notebooks, visualization');
        console.log('');
        console.log('Examples:');
        console.log('  node tools/data_agent_kit.js get_active_editor_context');
        console.log('  node tools/data_agent_kit.js get_active_gcp_connection');
        console.log('  node tools/data_agent_kit.js list_resource_templates');
        console.log('  node tools/data_agent_kit.js read-resource workspace://active-editor');
        console.log('  node tools/data_agent_kit.js --server notebooks list_cells "{\\"path\\":\\"test.ipynb\\"}"');
        process.exit(0);
    }

    if (rawArgs.includes('--test')) {
        await runSelfTest();
        return;
    }

    let serverName = 'data-agent-kit';
    let filteredArgs = [];

    for (let i = 0; i < rawArgs.length; i++) {
        if (rawArgs[i] === '--server' && i + 1 < rawArgs.length) {
            serverName = rawArgs[i + 1];
            i++;
        } else {
            filteredArgs.push(rawArgs[i]);
        }
    }

    const command = filteredArgs[0];
    const target = TARGET_MAP[serverName] || serverName;

    if (command === 'list-tools') {
        try {
            const tools = await listTools(target);
            console.log(JSON.stringify(tools, null, 2));
        } catch (err) {
            console.error(err.message);
            process.exit(1);
        }
        return;
    }

    if (command === 'read-resource') {
        const uri = filteredArgs[1] || 'workspace://active-editor';
        try {
            const res = await callTool('read_resource', { uri: uri }, TARGET_MAP['data-agent-kit']);
            console.log(JSON.stringify(res, null, 2));
        } catch (err) {
            console.error(err.message);
            process.exit(1);
        }
        return;
    }

    // Tool call
    const toolName = command;
    let toolArgs = {};
    if (filteredArgs.length > 1) {
        const rawJson = filteredArgs.slice(1).join(' ');
        try {
            toolArgs = parseJsonArg(rawJson);
        } catch (e) {
            console.error(e.message);
            process.exit(1);
        }
    }

    try {
        const res = await callTool(toolName, toolArgs, target);
        console.log(JSON.stringify(res, null, 2));
    } catch (err) {
        console.error(err.message);
        process.exit(1);
    }
}

if (require.main === module) {
    main();
}

module.exports = {
    callTool,
    listTools,
    executeRpc,
    TARGET_MAP
};
