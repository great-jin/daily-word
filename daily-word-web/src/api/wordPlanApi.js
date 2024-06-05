import request from './core/axios';

const apiPrefix = "/api/server/wordPlan"

export function selectOne(params) {
    return request({
        url: `${apiPrefix}/selectOne?id=${params}`,
        method: 'get'
    })
}

export function insert(params) {
    return request({
        url: `${apiPrefix}/insert`,
        method: 'post',
        data: params
    })
}

export function update(params) {
    return request({
        url: `${apiPrefix}/update`,
        method: 'get',
        data: params
    })
}
