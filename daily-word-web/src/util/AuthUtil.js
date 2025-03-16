const userId = 'U_ID'
const userAuth = 'U_AUTH'
const userToken = 'U_TOKEN'

export function setToken(data) {
    localStorage.setItem(userId, data.userId)
    localStorage.setItem(userAuth, data.authentic)
    localStorage.setItem(userToken, data.refreshToken)
}

export function getUId() {
    return localStorage.getItem(userId)
}

export function getToken() {
    let token = localStorage.getItem(userToken)
    if (token === undefined || token === null) {
        token = ''
    }
    let auth = localStorage.getItem(userAuth)
    if (auth === undefined || auth === null) {
        auth = ''
    } else {
        auth = 'Basic ' + auth
    }
    return [token, auth]
}

export function clearToken() {
    localStorage.removeItem(userId)
    localStorage.removeItem(userToken)
    localStorage.removeItem(userAuth)
}