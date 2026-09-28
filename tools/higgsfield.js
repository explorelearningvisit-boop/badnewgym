const fs = require('fs');
const path = require('path');
const https = require('https');

const MCP_AUTH_DIR = path.join(process.env.USERPROFILE || 'C:\\Users\\User', '.mcp-auth', 'mcp-remote-v1');
const HIGGSFIELD_ENDPOINT = 'https://mcp.higgsfield.ai/mcp';

function getAccessToken() {
    if (!fs.existsSync(MCP_AUTH_DIR)) {
        throw new Error(`MCP auth directory not found: ${MCP_AUTH_DIR}`);
    }
    const files = fs.readdirSync(MCP_AUTH_DIR);
    const tokenFile = files.find(f => f.endsWith('_tokens.json'));
    if (!tokenFile) {
        throw new Error(`No token file found in ${MCP_AUTH_DIR}`);
    }
    const tokenData = JSON.parse(fs.readFileSync(path.join(MCP_AUTH_DIR, tokenFile), 'utf8'));
    return tokenData.access_token;
}

async function callHiggsfieldRpc(method, params = {}, id = 1) {
    const token = getAccessToken();
    const url = new URL(HIGGSFIELD_ENDPOINT);
    const payload = JSON.stringify({
        jsonrpc: '2.0',
        method,
        params,
        id
    });

    return new Promise((resolve, reject) => {
        const req = https.request(url, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Accept': 'application/json, text/event-stream',
                'Content-Length': Buffer.byteLength(payload)
            }
        }, (res) => {
            let data = '';
            res.on('data', chunk => data += chunk);
            res.on('end', () => {
                if (res.statusCode >= 400) {
                    return reject(new Error(`HTTP ${res.statusCode}: ${data}`));
                }
                const lines = data.split('\n');
                for (const line of lines) {
                    if (line.startsWith('data: ')) {
                        try {
                            const parsed = JSON.parse(line.slice(6));
                            return resolve(parsed);
                        } catch (e) {
                            // continue parsing
                        }
                    }
                }
                try {
                    const parsed = JSON.parse(data);
                    resolve(parsed);
                } catch (e) {
                    resolve({ raw: data });
                }
            });
        });

        req.on('error', reject);
        req.write(payload);
        req.end();
    });
}

async function main() {
    const args = process.argv.slice(2);
    if (args.length === 0) {
        console.log('Usage: node higgsfield.js <toolName> [jsonArgs]');
        console.log('       node higgsfield.js --list');
        console.log('       node higgsfield.js balance');
        process.exit(0);
    }

    if (args[0] === '--list') {
        const res = await callHiggsfieldRpc('tools/list');
        const tools = res.result?.tools || [];
        console.log(`Available Higgsfield tools (${tools.length}):`);
        tools.forEach(t => console.log(` - ${t.name}: ${t.description?.slice(0, 80)}...`));
        return;
    }

    const toolName = args[0];
    let toolArgs = {};
    if (args.length > 1) {
        const raw = args.slice(1).join(' ');
        try {
            toolArgs = JSON.parse(raw);
        } catch (e) {
            // Attempt to parse key:value or relaxed json
            try {
                const formatted = raw
                    .replace(/([a-zA-Z0-9_]+)\s*:\s*([a-zA-Z0-9_]+)/g, '"$1":"$2"')
                    .replace(/(['"])?([a-zA-Z0-9_]+)(['"])?:/g, '"$2":')
                    .replace(/'/g, '"');
                toolArgs = JSON.parse(formatted);
            } catch (e2) {
                console.error('Invalid JSON for tool arguments:', raw);
                process.exit(1);
            }
        }
    }

    try {
        const res = await callHiggsfieldRpc('tools/call', {
            name: toolName,
            arguments: toolArgs
        });

        if (res.error) {
            console.error('Higgsfield Error:', JSON.stringify(res.error, null, 2));
            process.exit(1);
        }

        const result = res.result;
        if (result?.content) {
            result.content.forEach(c => {
                if (c.type === 'text') console.log(c.text);
            });
        }
        if (result?.structuredContent) {
            console.log(JSON.stringify(result.structuredContent, null, 2));
        } else if (!result?.content) {
            console.log(JSON.stringify(result, null, 2));
        }
    } catch (err) {
        console.error('Execution Failed:', err.message);
        process.exit(1);
    }
}

main();
