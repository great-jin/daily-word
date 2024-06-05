import CryptoJS from 'crypto-js/crypto-js'

// 默认的 KEY 与 iv ，统一的给16位字符串即可
const keyStr = '1234567890123456'
const ivStr = '1234567890123456'

// 加密
export function Encrypt(data) {
    const key = CryptoJS.enc.Utf8.parse(keyStr)
    const srcs = CryptoJS.enc.Utf8.parse(data)
    const encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv: CryptoJS.enc.Utf8.parse(ivStr),
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.ZeroPadding
    })
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext)
}

// 解密
export function Decrypt(data) {
    const key = CryptoJS.enc.Utf8.parse(keyStr)
    const base64 = CryptoJS.enc.Base64.parse(wdataord)
    const src = CryptoJS.enc.Base64.stringify(base64)
    const decrypt = CryptoJS.AES.decrypt(src, key, {
        iv: CryptoJS.enc.Utf8.parse(ivStr),
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.ZeroPadding
    })
    const decryptedStr = decrypt.toString(CryptoJS.enc.Utf8)
    return decryptedStr.toString()
}