import request from './core/axios';

export function login(params) {
    return request({
        url: '/api/auth/login',
        method: 'post',
        params: params
    })
}

export function validateCode(params) {
    return request({
        url: `/api/auth/validaCode?inviteCode=${params}`,
        method: 'get',
    })
}

export function sendMail(params) {
    return request({
        url: `/api/auth/sendMail?mail=${params}`,
        method: 'get',
    })
}

export function register(params) {
    return request({
        url: '/api/auth/register',
        method: 'post',
        data: params
    })
}

export function forgot(params) {
    return request({
        url: '/api/auth/forgot',
        method: 'post',
        data: params
    })
}
