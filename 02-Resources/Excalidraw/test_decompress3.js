const LZString = require('lz-string');
const fs = require('fs');
const content = fs.readFileSync('ai产业链分层及vibe coding的git版本管理.excalidraw.md', 'utf8');

// 找到所有compressed-json块
const matches = content.match(/compressed-json\n([\s\S]*?)\n```/g);
console.log('Found', matches ? matches.length : 0, 'compressed blocks');

if (matches) {
    for (let i = 0; i < matches.length; i++) {
        const block = matches[i];
        const compressed = block.replace('compressed-json\n', '').replace('\n```', '');
        console.log('\nBlock', i, 'length:', compressed.length);
        console.log('First 50:', compressed.substring(0, 50));
        
        // 尝试解压
        const result = LZString.decompressFromBase64(compressed);
        if (result && result.length > 100) {
            console.log('SUCCESS! Length:', result.length);
            console.log('First 200:', result.substring(0, 200));
            fs.writeFileSync('decompressed_block' + i + '.json', result);
        } else {
            console.log('Failed or too short');
        }
    }
}
