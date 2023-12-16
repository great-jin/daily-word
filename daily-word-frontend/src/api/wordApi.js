import request from './core/axios';

const apiPrefix = "/api/business/word"

export function translate(params) {
    return request({
        url: `${apiPrefix}/translation?word=${params}`,
        method: 'get'
    })
}

export function getTaskContent(params) {
    return request({
        url: `${apiPrefix}/getTaskContent`,
        method: 'post',
        data: params
    })
}
