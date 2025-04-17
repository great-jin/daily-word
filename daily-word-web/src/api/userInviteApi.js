import request from './core/axios';

const apiPrefix = "/api/server/userInvite"

export function countRequest() {
    return request({
        url: `${apiPrefix}/count`,
        method: 'get'
    })
}
export function searchUser(params) {
    return request({
        url: `${apiPrefix}/search?username=${params}`,
        method: 'get'
    })
}

export function listRequests(params) {
    return request({
        url: `${apiPrefix}/list?inviteType=${params}`,
        method: 'get',
    })
}

export function sendInvite(params) {
    return request({
        url: `${apiPrefix}/sendInvite?userId=${params}`,
        method: 'get',
    })
}

export function handleInvite(params) {
    return request({
        url: `${apiPrefix}/handleInvite`,
        method: 'get',
        params: params
    })
}

