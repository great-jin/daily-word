import request from './core/axios';

const prefix = "/api/server/rankBoard"

export function getUserRank() {
    return request({
        url: `${prefix}/getUserRank`,
        method: 'get'
    })
}

export function getRankByUid(params) {
    return request({
        url: `${prefix}/getRankByUid?userId=${params}`,
        method: 'get'
    })
}

export function list(params) {
    return request({
        url: `${prefix}/list?catalog=${params}`,
        method: 'get'
    })
}

