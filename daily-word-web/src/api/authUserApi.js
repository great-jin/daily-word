import request from './core/axios';

const apiPrefix = "/api/auth"

export function login(params) {
    return request({
        url: `${apiPrefix}/login`,
        method: 'post',
        params: params
    })
}

export function validateCode(params) {
    return request({
        url: `${apiPrefix}/validaCode?inviteCode=${params}`,
        method: 'get',
    })
}

export function sendMail(params) {
    return request({
        url: `${apiPrefix}/sendMail`,
        method: 'get',
        params: params
    })
}

export function register(params) {
    return request({
        url: `${apiPrefix}/register`,
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

export function destroyAccount() {
    return request({
        url: '/api/auth/destroy',
        method: 'get',
    })
}
