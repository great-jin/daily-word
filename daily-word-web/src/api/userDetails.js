import request from './core/axios';

const prefix = "/api/server/userDetail"

export function getByUserId(params) {
    return request({
        url: `${prefix}/${params}`,
        method: 'get'
    })
}

