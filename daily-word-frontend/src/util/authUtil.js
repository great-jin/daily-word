export function setToken(auth, token) {
    localStorage.setItem("USER_TOKEN", token)
    sessionStorage.setItem("USER_AUTH", auth)
}

export function getToken() {
    let token = localStorage.getItem('USER_TOKEN')
    if (token === undefined || token === null) {
        token = ''
    }
    let auth = sessionStorage.getItem('USER_AUTH')
    if (auth === undefined || auth === null) {
        auth = ''
    }
    return [token, auth]
}

export function clearToken() {
    localStorage.removeItem('USER_TOKEN')
    sessionStorage.removeItem('USER_AUTH')
}