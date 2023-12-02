import request from './axios';

export function login(params) {
    return request({
        url: '/api/auth/verify',
        method: 'post',
        params: params
    })
}
