import request from './core/axios';

const apiPrefix = "/api/server/match"

export function submit(params) {
    return request({
        url: `${apiPrefix}/submit`,
        method: 'post',
        data: params
    })
}

export function listMatchHistory(params) {
    return request({
        url: `${apiPrefix}/listHistory`,
        method: 'get',
        params: params
    })
}
