import request from './core/axios';

const apiPrefix = "/api/server/match"

export function validate(params) {
    return request({
        url: `${apiPrefix}/validate?catalog=${params}`,
        method: 'get'
    })
}

export function checkMatchStatus(params) {
    return request({
        url: `${apiPrefix}/checkFinished?matchId=${params}`,
        method: 'get'
    })
}

export function submitTask(params) {
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

export function getMatchDetail(params) {
    return request({
        url: `${apiPrefix}/getDetail?matchId=${params}`,
        method: 'get'
    })
}