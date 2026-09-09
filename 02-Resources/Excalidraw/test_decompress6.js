const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 提取compressed-json块 - 更精确的模式
const lines = content.split('\n');
let inCompressed = false;
let compressedLines = [];

for (const line of lines) {
    if (line.includes('compressed-json')) {
        inCompressed = true;
        continue;
    }
    if (inCompressed && line.startsWith('```')) {
        break;
    }
    if (inCompressed) {
        compressedLines.push(line);
    }
}

const compressed = compressedLines.join('\n');
console.log('Compressed length:', compressed.length);
console.log('First 100:', compressed.substring(0, 100));
console.log('Last 100:', compressed.substring(compressed.length - 100));

// base64解码
const decoded = Buffer.from(compressed, 'base64');
console.log('Decoded length:', decoded.length);

// 尝试用zlib的各种方式
const zlib = require('zlib');

// 尝试gunzip
try {
    const result = zlib.gunzipSync(decoded);
    console.log('gunzip SUCCESS:', result.length);
    console.log(result.toString('utf8').substring(0, 200));
} catch (e) {
    console.log('gunzip failed:', e.message);
}

// 尝试inflate
try {
    const result = zlib.inflateSync(decoded);
    console.log('inflate SUCCESS:', result.length);
} catch (e) {
    console.log('inflate failed:', e.message);
}

// 尝试inflateRaw
try {
    const result = zlib.inflateRawSync(decoded);
    console.log('inflateRaw SUCCESS:', result.length);
} catch (e) {
    console.log('inflateRaw failed:', e.message);
}

// 尝试brotli
try {
    const result = zlib.brotliDecompressSync(decoded);
    console.log('brotli SUCCESS:', result.length);
} catch (e) {
    console.log('brotli failed:', e.message);
}
