import {Decrypt, Encrypt} from "@/util/AES";

const KEY_UID = 'u0'
const KEY_AUTH = 'u1'
const KEY_TOKEN = 'u2'

export function setToken(data) {
    localStorage.setItem(KEY_UID, data.user)
    localStorage.setItem(KEY_AUTH, data.authentic)
    localStorage.setItem(KEY_TOKEN, data.refreshToken)
}

export function getUid() {
    let uid = localStorage.getItem(KEY_UID)
    if (uid === undefined || uid === null) {
        return null
    }

    return Decrypt(uid)
}

export function getToken() {
    let token = localStorage.getItem(KEY_TOKEN)
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

export function clearToken() {
    localStorage.removeItem(KEY_UID)
    localStorage.removeItem(KEY_AUTH)
    localStorage.removeItem(KEY_TOKEN)
}