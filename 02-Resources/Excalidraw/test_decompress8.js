const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 提取compressed-json块
const match = content.match(/compressed-json\n([\s\S]*?)\n```/);
const compressed = match[1];

// base64解码
const decoded = Buffer.from(compressed, 'base64');

// 检查是否是某种自定义压缩格式
// 前几个字节: 37 82 80 90 04 40 2e 09 e0 0e
// 看起来不像标准的zlib/gzip头

// 尝试用不同的方式解析
// 可能是经过某种编码后再压缩的

// 尝试用lz-string的Python实现
// 先安装lz-string npm包并尝试
const LZString = require('lz-string');

// 尝试不同的解码方式
console.log('Trying lz-string decode...');

// 尝试用UTF16解码
const utf16Result = LZString.decompressFromUTF16(compressed);
if (utf16Result && utf16Result.length > 100) {
    console.log('UTF16 SUCCESS:', utf16Result.length);
    console.log(utf16Result.substring(0, 200));
}

// 尝试用自定义字典解码
const customDict = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
const customResult = LZString.decompressFromBase64(compressed, customDict);
if (customResult && customResult.length > 100) {
    console.log('Custom dict SUCCESS:', customResult.length);
}

// 尝试用不同的base64变体
// URL-safe base64
const urlSafe = compressed.replace(/-/g, '+').replace(/_/g, '/');
const urlSafeResult = LZString.decompressFromBase64(urlSafe);
if (urlSafeResult && urlSafeResult.length > 100) {
    console.log('URL-safe SUCCESS:', urlSafeResult.length);
}

// 尝试去掉前几个字节后再解压
for (let skip = 0; skip < 10; skip++) {
    const skipped = decoded.slice(skip);
    const skippedB64 = skipped.toString('base64');
    const result = LZString.decompressFromBase64(skippedB64);
    if (result && result.length > 100) {
        console.log('Skip', skip, 'SUCCESS:', result.length);
        break;
    }
}

console.log('All lz-string attempts done');
