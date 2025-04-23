import request from './core/axios';

const apiPrefix = "/api/server/engine"

export function callTranslate(params) {
    return request({
        url: `${apiPrefix}/translate`,
        method: 'post',
        data: params
    })
}