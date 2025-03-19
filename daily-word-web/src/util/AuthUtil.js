const userAuth = 'U_A'
const userToken = 'U_T'

export function setToken(data) {
    localStorage.setItem(userAuth, data.authentic)
    localStorage.setItem(userToken, data.refreshToken)
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
    localStorage.removeItem(userToken)
    localStorage.removeItem(userAuth)
}