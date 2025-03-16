import request from './core/axios';

const prefix = "/api/server/rankBoard"

export function getUserRank(params) {
    return request({
        url: `${prefix}/getUserRank?userId=${params}`,
        method: 'get'
    })
}

export function list(params) {
    return request({
        url: `${prefix}/list?userId=${params.userId}&catalog=${params.catalog}`,
        method: 'get'
    })
}

