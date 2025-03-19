import request from './core/axios';

const prefix = "/api/server/rankBoard"

export function getUserRank() {
    return request({
        url: `${prefix}/getUserRank`,
        method: 'get'
    })
}

export function list(params) {
    return request({
        url: `${prefix}/list?catalog=${params}`,
        method: 'get'
    })
}

