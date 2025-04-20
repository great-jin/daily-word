// 是否为邮箱
export function isEmail(email) {
    return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
}

// 只包含字母和数字
export function isNameValid(username) {
    return /^[A-Za-z0-9]+$/.test(username);
}

// 密码合规校验
export function isPwdLegal(pwd1, pwd2) {
    if (pwd1 === null || pwd1 === '' || pwd2 === null || pwd2 === '') {
        return '密码不能为空！'
    }
    if (pwd1 !== pwd2) {
        return '两次密码不一致，请检查后重试！'
    }

    if (pwd1.length < 6 || pwd1.length > 50) {
        return '密码长度需大于 6 位且小于 50 位!'
    }

    // 弱密码校验
    if (/^\d+$/.test(pwd1)) {
        return '密码不允许为纯数字！'
    }
    if (/^[a-zA-Z]+$/.test(pwd1)) {
        return '密码不允许为纯字母！'
    }

    if (!(/^[A-Za-z0-9.!#]+$/.test(pwd1))) {
        return '密码无效，特殊符号仅支持 (. ! #) 三者'
    }
    return null
}