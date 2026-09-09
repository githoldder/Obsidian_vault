const LZString = require('lz-string');
const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');
const match = content.match(/compressed-json\n(.*?)\n```/s);
if (match) {
    const compressed = match[1];
    console.log('Compressed length:', compressed.length);
    console.log('First 100:', compressed.substring(0, 100));
    
    // 尝试所有解压方法
    const methods = [
        'decompressFromBase64',
        'decompressFromEncodedURIComponent', 
        'decompressFromUTF16',
        'decompress'
    ];
    
    for (const method of methods) {
        try {
            const result = LZString[method](compressed);
            if (result && result.length > 1) {
                console.log(method + ' SUCCESS, length:', result.length);
                console.log('First 200:', result.substring(0, 200));
                fs.writeFileSync('decompressed_' + method + '.json', result);
            } else {
                console.log(method + ' failed or empty');
            }
        } catch (e) {
            console.log(method + ' error:', e.message);
        }
    }
} else {
    console.log('No match found');
}
