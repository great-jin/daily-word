import request from './core/axios';

const prefix = "/api/server/userFriend"

export function list(params) {
    return request({
        url: `${prefix}/list?userId=${params}`,
        method: 'get'
    })
}

