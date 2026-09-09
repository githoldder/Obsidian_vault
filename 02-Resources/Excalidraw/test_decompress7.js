const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 提取compressed-json块
const match = content.match(/compressed-json\n([\s\S]*?)\n```/);
const compressed = match[1];

// base64解码
const decoded = Buffer.from(compressed, 'base64');

// 检查前几个字节
console.log('First 10 bytes (hex):', decoded.slice(0, 10).toString('hex'));
console.log('First 10 bytes:', decoded.slice(0, 10));

// 尝试不同的解压方式
const zlib = require('zlib');

// 尝试用不同的windowBits
try {
    const result = zlib.inflateSync(decoded, { windowBits: 15 });
    console.log('inflate w15 SUCCESS:', result.length);
} catch (e) {}

try {
    const result = zlib.inflateSync(decoded, { windowBits: -15 });
    console.log('inflate w-15 SUCCESS:', result.length);
} catch (e) {}

try {
    const result = zlib.inflateSync(decoded, { windowBits: 31 });
    console.log('inflate w31 SUCCESS:', result.length);
} catch (e) {}

// 尝试用pako的自定义选项
const pako = require('pako');
try {
    const result = pako.inflate(decoded, { windowBits: 15 });
    console.log('pako w15 SUCCESS:', result.length);
} catch (e) {}

try {
    const result = pako.inflate(decoded, { windowBits: -15 });
    console.log('pako w-15 SUCCESS:', result.length);
} catch (e) {}

try {
    const result = pako.inflate(decoded, { windowBits: 31 });
    console.log('pako w31 SUCCESS:', result.length);
} catch (e) {}

// 尝试用不同的to参数
try {
    const result = pako.inflate(decoded, { to: 'string' });
    console.log('pako to string SUCCESS:', result.length);
} catch (e) {}

// 尝试用raw inflate with dictionary
try {
    const result = pako.inflateRaw(decoded, { windowBits: 15 });
    console.log('pako raw w15 SUCCESS:', result.length);
} catch (e) {}

console.log('Done');
