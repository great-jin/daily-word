const userAuth = 'USER_AUTH'
const userToken = 'USER_TOKEN'

export function setToken(auth, token) {
    localStorage.setItem(userToken, token)
    sessionStorage.setItem(userAuth, auth)
}

export function getToken() {
    let token = localStorage.getItem(userToken)
    if (token === undefined || token === null) {
        token = ''
    }
    let auth = sessionStorage.getItem(userAuth)
    if (auth === undefined || auth === null) {
        auth = ''
    } else {
        auth = 'Basic ' + auth
    }
    return [token, auth]
}

export function clearToken() {
    localStorage.removeItem(userToken)
    sessionStorage.removeItem(userAuth)
}