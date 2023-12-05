// 获取可用的语音列表
// voices[4]: Google US English
// voices[5]: Google UK English Female
// voices[19]: Google 普通话（中国大陆）
// voices[20]: Google 粤語（香港）
const voices = window.speechSynthesis.getVoices();

export function speakEn(text, country) {
    const utterance = new SpeechSynthesisUtterance(text);
    // 选择音色
    let countryNum = 4
    if (country == null) {
        countryNum = 4
    } else {
        countryNum === 'US' ? 4 : 5
    }
    // 选择第一个语音
    utterance.voice = voices[countryNum];
    // 设置速率
    utterance.rate = 0.8;
    speechSynthesis.speak(utterance);
}

export function speakZh(text, country) {
    const utterance = new SpeechSynthesisUtterance(text);
    // 选择音色
    let countryNum = 19
    if (country == null) {
        countryNum = 19
    } else {
        countryNum === 'HK' ? 20 : 19
    }
    utterance.voice = voices[countryNum];
    // 设置速率
    utterance.rate = 0.8;
    speechSynthesis.speak(utterance);
}