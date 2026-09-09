const LZString = require('lz-string');
const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');
const match = content.match(/compressed-json\n(.*?)\n```/s);
if (match) {
    const compressed = match[1];
    console.log('Compressed length:', compressed.length);
    
    let result = LZString.decompressFromBase64(compressed);
    if (result) {
        console.log('decompressFromBase64 SUCCESS, length:', result.length);
        console.log('First 500:', result.substring(0, 500));
        fs.writeFileSync('decompressed.json', result);
        console.log('Saved to decompressed.json');
    } else {
        console.log('decompressFromBase64 failed');
    }
} else {
    console.log('No match found');
}
