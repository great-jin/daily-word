import CryptoJS from 'crypto-js/crypto-js'

// 默认的 KEY 与 IV ，统一的给16位字符串即可
const IV = 'budai_invincible'
const KEY = 'budai_invincible'

// 加密
export function Encrypt(data) {
    const encrypted = CryptoJS.AES.encrypt(
        CryptoJS.enc.Utf8.parse(data),
        CryptoJS.enc.Utf8.parse(KEY),
        {
            iv: CryptoJS.enc.Utf8.parse(IV),
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.ZeroPadding
        }
    )
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext)
}

// 解密
export function Decrypt(data) {
    const decrypted = CryptoJS.AES.decrypt(
        {
            // 还原 Base64
            ciphertext: CryptoJS.enc.Base64.parse(data)
        },
        CryptoJS.enc.Latin1.parse(KEY),
        {
            iv: CryptoJS.enc.Latin1.parse(IV),
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.ZeroPadding
        }
    )
    return decrypted.toString(CryptoJS.enc.Latin1).replace(/\0+$/g, '');
}

export function MD5(data) {
    return CryptoJS.MD5(data).toString()
}

export function SHA256(data) {
    return CryptoJS.SHA256(data).toString()
}
