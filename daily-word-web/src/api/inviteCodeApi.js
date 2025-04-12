import request from './core/axios';

const apiPrefix = "/api/server/inviteCode"

export function listInviteCode() {
    return request({
        url: `${apiPrefix}/list`,
        method: 'get'
    })
}

export function generateCode() {
    return request({
        url: `${apiPrefix}/generate`,
        method: 'get'
    })
}

export function deleteCode(id) {
    return request({
        url: `${apiPrefix}/delete?id=${id}`,
        method: 'get'
    })
}

export function reactiveCode(id) {
    return request({
        url: `${apiPrefix}/reactive?id=${id}`,
        method: 'get'
    })
}
