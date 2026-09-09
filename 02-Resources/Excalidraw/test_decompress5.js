const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 提取compressed-json块
const match = content.match(/compressed-json\n([\s\S]*?)\n```/);
const compressed = match[1];

// base64解码
const decoded = Buffer.from(compressed, 'base64');
console.log('Decoded length:', decoded.length);

// 尝试用不同的偏移量解压
const pako = require('pako');
const zlib = require('zlib');

for (let offset = 0; offset < 20; offset++) {
    const sliced = decoded.slice(offset);
    
    // pako inflateRaw with different options
    try {
        const result = pako.inflateRaw(sliced, { windowBits: 15 });
        if (result.length > 1000) {
            console.log('SUCCESS offset=', offset, 'length=', result.length);
            const str = result.toString('utf8');
            console.log('First 200:', str.substring(0, 200));
            fs.writeFileSync('decompressed_success.json', str);
            process.exit(0);
        }
    } catch (e) {}
    
    // zlib inflateRaw
    try {
        const result = zlib.inflateRawSync(sliced);
        if (result.length > 1000) {
            console.log('ZLIB SUCCESS offset=', offset, 'length=', result.length);
            const str = result.toString('utf8');
            console.log('First 200:', str.substring(0, 200));
            fs.writeFileSync('decompressed_success.json', str);
            process.exit(0);
        }
    } catch (e) {}
}

console.log('All offsets failed');
