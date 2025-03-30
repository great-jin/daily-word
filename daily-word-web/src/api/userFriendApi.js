import request from './core/axios';

const prefix = "/api/server/userFriend"

export function list(params) {
    return request({
        url: `${prefix}/list`,
        method: 'get'
    })
}

export function deleteById(params) {
    return request({
        url: `${prefix}/deleteById?friendId=${params}`,
        method: 'get',
    })
}

