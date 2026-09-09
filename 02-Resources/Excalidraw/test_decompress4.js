const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 提取compressed-json块
const match = content.match(/compressed-json\n([\s\S]*?)\n```/);
if (!match) {
    console.log('No match');
    process.exit(1);
}

const compressed = match[1];
console.log('Compressed length:', compressed.length);

// 尝试用pako解压
const pako = require('pako');

// base64解码
const decoded = Buffer.from(compressed, 'base64');
console.log('Decoded length:', decoded.length);
console.log('First 20 bytes:', decoded.slice(0, 20).toString('hex'));

// 尝试解压
try {
    const result = pako.inflate(decoded);
    console.log('pako.inflate SUCCESS, length:', result.length);
    const str = result.toString('utf8');
    console.log('First 200:', str.substring(0, 200));
    fs.writeFileSync('decompressed_pako.json', str);
} catch (e) {
    console.log('pako.inflate failed:', e.message);
}

try {
    const result = pako.inflateRaw(decoded);
    console.log('pako.inflateRaw SUCCESS, length:', result.length);
    const str = result.toString('utf8');
    console.log('First 200:', str.substring(0, 200));
    fs.writeFileSync('decompressed_pako_raw.json', str);
} catch (e) {
    console.log('pako.inflateRaw failed:', e.message);
}

try {
    const result = pako.ungzip(decoded);
    console.log('pako.ungzip SUCCESS, length:', result.length);
} catch (e) {
    console.log('pako.ungzip failed:', e.message);
}
