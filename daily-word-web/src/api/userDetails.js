import request from './core/axios';

const prefix = "/api/server/userDetail"

export function getDetails() {
    return request({
        url: `${prefix}/get`,
        method: 'get'
    })
}

