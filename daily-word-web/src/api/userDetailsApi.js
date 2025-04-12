import request from './core/axios';

const prefix = "/api/server/userDetail"

export function getDetails() {
    return request({
        url: `${prefix}/getById`,
        method: 'get'
    })
}

export function uploadAvatar(params) {
    return request({
        url: `${prefix}/uploadAvatar`,
        method: 'post',
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export function changePassword(params) {
    return request({
        url: `${prefix}/changePassword`,
        method: 'post',
        data: params
    })
}
