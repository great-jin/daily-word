import request from './core/axios';

const prefix = "/api/server/rankBoard"

export function list(params) {
    return request({
        url: `${prefix}/list?catalog=${params}`,
        method: 'get'
    })
}

