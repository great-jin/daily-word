import {Decrypt} from "@/util/EncryptUtil";

const KEY_UID = 'u0'
const KEY_AUTH = 'u1'
const KEY_JWT = 'u2'
const KEY_VERIFY = 'u3'

export function setToken(data) {
    localStorage.setItem(KEY_UID, data.key)
    localStorage.setItem(KEY_AUTH, data.authentic)
    localStorage.setItem(KEY_JWT, data.refreshToken)
    localStorage.setItem(KEY_VERIFY, data.verifyToken)
}

export function getUid(needDecode) {
    let uid = localStorage.getItem(KEY_UID)
    if (uid === undefined || uid === null) {
        return null
    }

    return needDecode ? Decrypt(uid) : uid
}

export function getToken() {
    let token = localStorage.getItem(KEY_JWT)
    if (token === undefined || token === null) {
        token = ''
    }
    let auth = localStorage.getItem(KEY_AUTH)
    if (auth === undefined || auth === null) {
        auth = ''
    } else {
        auth = 'Basic ' + auth
    }
    return [token, auth]
}

export function setRequestHeader(config) {
    // 请求添加默认请求头
    const token = getToken()
    if (token[0] !== '') {
        config.headers['Token'] = token[0]
    }
    if (token[1] !== '') {
        config.headers['Authorization'] = token[1]
    }

    let verifyToken = localStorage.getItem(KEY_VERIFY)
    if (verifyToken !== undefined && verifyToken !== null) {
        config.headers['verify-token'] = verifyToken
    }
    return config
}

export function isUseLogin() {
    const token = getToken()
    if (token === null || token.length === 0) {
        return false
    }

    const hasAuth = !(token[0] == null || token[0] === '')
    const hasJwt = !(token[1] == null || token[1] === '')

    return hasAuth && hasJwt
}

export function clearToken() {
    localStorage.removeItem(KEY_UID)
    localStorage.removeItem(KEY_AUTH)
    localStorage.removeItem(KEY_JWT)
}