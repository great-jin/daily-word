import CryptoJS from 'crypto-js/crypto-js'

export function MD5(data) {
    return CryptoJS.MD5(data).toString()
}

export function SHA256(data) {
    return CryptoJS.SHA256(data).toString()
}
