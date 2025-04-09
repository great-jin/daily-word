// 是否为邮箱
export function isEmail(email) {
    return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
}

// 只包含字母和数字
export function isNameValid(username) {
    return /^[A-Za-z0-9]+$/.test(username);
}

// 只包含字母和数字
// 特殊字符只包含 . ! #
export function isPwdValid(pwd) {
    return /^[A-Za-z0-9.!#]+$/.test(pwd);
}