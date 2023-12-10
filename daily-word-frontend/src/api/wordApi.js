import request from './core/axios';

export function translate(params) {
    return request({
        url: `/api/word/translation?word=${params}`,
        method: 'get'
    })
}

export function getTaskContent(params) {
    return request({
        url: `/api/word/getTaskContent`,
        method: 'post',
        data: params
    })
}
