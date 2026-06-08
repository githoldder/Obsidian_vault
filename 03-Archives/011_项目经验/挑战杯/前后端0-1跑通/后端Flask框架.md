#项目 
## 1. 环境准备
避免pycharm的环境依赖冲突，推荐的方式是一个任务创建一个虚拟环境，并在接下来的任务中创建虚拟环境

- **安装Python库**：
    
    ```bash
    pip install flask tts librosa plotly numpy
    ```
tts已经23年后就停止维护了，不支持python3.12，需要在下载时注明：
`pip install coqui-tts -i https://pypi.tuna.tsinghua.edu.cn/simple`

一起下载多个python库可能会导致很多报错问题,最稳妥的方式是一个一个安装,才方便问题排查
```
pip install flask -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install coqui-tts -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install librosa -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install plotly -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install numpy -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install requests -i https://pypi.tuna.tsinghua.edu.cn/simple
```
- **验证Ollama模型**：
    
    ```bash
    ollama run llama3:8b
    ollama run nomic-embed-text:latest
    ```
    
    输入测试提示词，确认响应。
```
taskkill /IM ollama.exe /F
成功: 已终止进程 "ollama.exe"，其 PID 为 1412。
$env:OLLAMA_HOST = "127.0.0.1:5000"
ollama serve    
```
1.终止ollama进程
2.修改ollama的服务网络端口号
3.运行ollama服务
- **创建目录**：
    
```
language-learning-app/
├── app.py
├── static/
│   ├── audio/
├── requirements.txt
```


```markdown
## 2. 后端开发

- **Flask应用**（app.py）：
    
    ```python
    from flask import Flask, request, jsonify, send_from_directory
    import os
    import uuid
    from TTS.api import TTS
    import librosa
    import numpy as np
    import plotly.graph_objects as go
    import requests
    
    app = Flask(__name__)
    audio_dir = os.path.join('static', 'audio')
    if not os.path.exists(audio_dir):
        os.makedirs(audio_dir)
    
    tts = TTS("tts_models/multilingual/multi-dataset/xtts_v2", gpu=False)
    
    @app.route('/generate-audio-spectrogram', methods=['POST'])
    def generate_audio_spectrogram():
        data = request.json
        text = data.get('text', '')
        if not text:
            return jsonify({'error': 'No text provided'}), 400
    
        # 可选：使用Llama 3改写文本
        response = requests.post('http://localhost:11434/api/generate', json={
            'model': 'llama3:8b',
            'prompt': f'将“{text}”改写为更自然的中文表达'
        })
        rewritten_text = response.json()['response'] if response.ok else text
    
        audio_filename = f"{uuid.uuid4()}.wav"
        audio_path = os.path.join(audio_dir, audio_filename)
        tts.tts_to_file(text=rewritten_text, file_path=audio_path, language="zh-cn")
    
        y, sr = librosa.load(audio_path)
        S = librosa.stft(y)
        S_db = librosa.amplitude_to_db(np.abs(S), ref=np.max)
        fig = go.Figure(data=go.Heatmap(z=S_db, colorscale='Viridis'))
        spectrogram_json = fig.to_json()
    
        audio_url = f"/static/audio/{audio_filename}"
        return jsonify({'sentence': rewritten_text, 'audio_url': audio_url, 'spectrogram': spectrogram_json})
    
    @app.route('/static/audio/<filename>')
    def serve_audio(filename):
        return send_from_directory(audio_dir, filename)
    
    if __name__ == '__main__':
        app.run(debug=True)
```


在与前端讨论过后，我们得出的结论是：

接下来的后端以及实现的功能是：
1.用户通过点击前端交互的按钮可以进行录音
2.录音保存好后前端发给后端
3.后端将音频存储在服务器
4.进行语图渲染，渲染完成后返回语图渲染的结果给前端
5.音频数据提前预处理，即将词组根据各自的语图生成，形成知识库/语料库
先进行向量嵌入
调用讯飞星火api/TTS模型功能对语图进行语图分析
也就是说，我现在的任务就转化成了，作为本地服务器（后续会变成在其他服务器上挂载）
将音频文件从前端接收，存储在本地目录；然后使用运行语图生成脚本，按照脚本规则对输入的音频进行语图生成；接着将渲染好的语图返回给前端，前端再将其渲染出来；
为此我需要先进行语料库的搭建，也就是说先将所有的词组音频都进行语图分析，并且将它们都生成对应的语图，存储起来，构建成语料库，供AI进行分析。
现在我们先把这个任务根据OKR进行拆解，拆解成一个个关键的可验收的节点结果
再拆分，运用逆向思维，思考从我的现状（脚本可以对本地存储的音频进行语图渲染，并生成png图片保存在本地），到目标还需要几步
