/**
 * Stitch MCP Bridge & CLI Tool
 * Connects directly to Stitch MCP via mcp-remote:
 *  - Endpoint: https://stitch.googleapis.com/mcp
 *  - Header: X-Goog-Api-Key (loaded dynamically from local config or STITCH_API_KEY env)
 */

const { spawn } = require('child_process');
const fs = require('fs');
const path = require('path');

const STITCH_URL = 'https://stitch.googleapis.com/mcp';

function getStitchApiKey() {
    if (process.env.STITCH_API_KEY) {
        return process.env.STITCH_API_KEY;
    }
    const configPath = path.join(process.env.USERPROFILE || 'C:\\Users\\User', '.gemini', 'config', 'mcp_config.json');
    if (fs.existsSync(configPath)) {
        try {
            const config = JSON.parse(fs.readFileSync(configPath, 'utf8'));
            const args = config.mcpServers?.StitchMCP?.args || [];
            const headerIdx = args.findIndex(a => typeof a === 'string' && a.toLowerCase().includes('x-goog-api-key:'));
            if (headerIdx !== -1) {
                return args[headerIdx].split(':')[1].trim();
            }
            const flagIdx = args.indexOf('--header');
            if (flagIdx !== -1 && args[flagIdx + 1] && args[flagIdx + 1].toLowerCase().startsWith('x-goog-api-key:')) {
                return args[flagIdx + 1].split(':')[1].trim();
            }
        } catch (e) {
            // ignore config parse errors
        }
    }
    return '';
}

function executeStitchRpc(method, params = {}) {
    return new Promise((resolve, reject) => {
        const apiKey = getStitchApiKey();
        if (!apiKey) {
            return reject(new Error('Stitch API key not found in environment (STITCH_API_KEY) or mcp_config.json.'));
        }

        const child = spawn('npx.cmd', [
            'mcp-remote',
            STITCH_URL,
            '--header',
            `X-Goog-Api-Key: ${apiKey}`
        ], {
            shell: true,
            stdio: ['pipe', 'pipe', 'pipe']
        });

        let output = '';
        let errOutput = '';
        let initDone = false;
        let responseReceived = false;

        const timeout = setTimeout(() => {
            child.kill();
            if (!responseReceived) {
                reject(new Error(`Stitch MCP request '${method}' timed out after 30s.`));
            }
        }, 30000);

        child.stdout.on('data', (data) => {
            output += data.toString();
            const lines = output.split('\n');
            for (let i = 0; i < lines.length - 1; i++) {
                const line = lines[i].trim();
                if (!line) continue;
                try {
                    const parsed = JSON.parse(line);
                    if (parsed.id === 1) {
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
                    // Ignore non-JSON lines
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
                reject(new Error(`Stitch process exited with code ${code}. Stderr: ${errOutput}`));
            }
        });

        child.on('error', (err) => {
            clearTimeout(timeout);
            reject(err);
        });

        // Initialize
        const initMsg = JSON.stringify({
            jsonrpc: '2.0',
            method: 'initialize',
            params: {
                protocolVersion: '2024-11-05',
                capabilities: {},
                clientInfo: { name: 'stitch-cli', version: '1.0' }
            },
            id: 1
        }) + '\n';
        child.stdin.write(initMsg);
    });
}

async function main() {
    const rawArgs = process.argv.slice(2);
    if (rawArgs.length === 0 || rawArgs.includes('--help') || rawArgs.includes('-h')) {
        console.log('Stitch MCP Bridge');
        console.log('Usage:');
        console.log('  node tools/stitch.js list-tools');
        console.log('  node tools/stitch.js <toolName> [jsonArgs]');
        console.log('');
        console.log('Examples:');
        console.log('  node tools/stitch.js list-tools');
        console.log('  node tools/stitch.js list_projects');
        process.exit(0);
    }

    const command = rawArgs[0];

    if (command === 'list-tools') {
        try {
            const res = await executeStitchRpc('tools/list', {});
            const tools = res.result?.tools || [];
            console.log(`Registered Stitch MCP tools (${tools.length}):`);
            tools.forEach(t => console.log(` - ${t.name}: ${t.description?.slice(0, 80)}...`));
        } catch (err) {
            console.error('Failed to list tools:', err.message);
            process.exit(1);
        }
        return;
    }

    const toolName = command;
    let toolArgs = {};
    if (rawArgs.length > 1) {
        const rawJson = rawArgs.slice(1).join(' ');
        try {
            toolArgs = JSON.parse(rawJson);
        } catch (e) {
            console.error('Invalid JSON for tool arguments:', rawJson);
            process.exit(1);
        }
    }

    try {
        const res = await executeStitchRpc('tools/call', { name: toolName, arguments: toolArgs });
        if (res.error) {
            console.error('Stitch Error:', JSON.stringify(res.error, null, 2));
            process.exit(1);
        }
        console.log(JSON.stringify(res.result, null, 2));
    } catch (err) {
        console.error('Execution failed:', err.message);
        process.exit(1);
    }
}

if (require.main === module) {
    main();
}

module.exports = {
    executeStitchRpc,
    getStitchApiKey
};

