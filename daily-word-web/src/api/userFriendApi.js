import request from './core/axios';

const apiPrefix = "/api/server/userFriend"

export function listFriends() {
    return request({
        url: `${apiPrefix}/list`,
        method: 'get'
    })
}

export function deleteById(params) {
    return request({
        url: `${apiPrefix}/deleteById?friendId=${params}`,
        method: 'get',
    })
}

