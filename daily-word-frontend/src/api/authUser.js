import request from './core/axios';

export function login(params) {
    return request({
        url: '/api/auth/login',
        method: 'post',
        params: params
    })
}
