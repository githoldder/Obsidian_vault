const fs = require('fs');
const LZString = require('lz-string');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 提取compressed-json块
const match = content.match(/compressed-json\n([\s\S]*?)\n```/);
const compressed = match[1];

// base64解码
const decoded = Buffer.from(compressed, 'base64');

// 去掉前几个字节后再用base64编码，然后用lz-string解压
for (let skip = 0; skip < 10; skip++) {
    const skipped = decoded.slice(skip);
    const skippedB64 = skipped.toString('base64');
    const result = LZString.decompressFromBase64(skippedB64);
    if (result && result.length > 100) {
        console.log('Skip', skip, 'SUCCESS:', result.length);
        console.log('First 500:', result.substring(0, 500));
        fs.writeFileSync('decompressed_skip' + skip + '.json', result);
        
        // 尝试解析JSON
        try {
            const json = JSON.parse(result);
            console.log('Valid JSON! Keys:', Object.keys(json));
            fs.writeFileSync('parsed_skip' + skip + '.json', JSON.stringify(json, null, 2));
        } catch (e) {
            console.log('Not valid JSON:', e.message);
        }
        break;
    }
}
