import request from './core/axios';

const apiPrefix = "/api/server/word"

export function translate(params) {
    return request({
        url: `${apiPrefix}/translation?word=${params}`,
        method: 'get'
    })
}

export function getDictDetail() {
    return request({
        url: `${apiPrefix}/getDictDetail`,
        method: 'get'
    })
}

export function startTask(params) {
    return request({
        url: `${apiPrefix}/startTask`,
        method: 'post',
        params: params
    })
}

export function getTaskAnswer(params) {
    return request({
        url: `${apiPrefix}/getAnswer?matchId=${params}`,
        method: 'get'
    })
}