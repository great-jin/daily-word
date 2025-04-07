import request from './core/axios';

const apiPrefix = "/api/server/rankBoard"

export function getUserRank() {
    return request({
        url: `${apiPrefix}/getUserRank`,
        method: 'get'
    })
}

export function getRankByUid(params) {
    return request({
        url: `${apiPrefix}/getRankByUid?userId=${params}`,
        method: 'get'
    })
}

export function list(params) {
    return request({
        url: `${apiPrefix}/list?catalog=${params}`,
        method: 'get'
    })
}

