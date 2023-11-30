import request from './axios';

export function testApi() {
    return request({
        url: '/api/test/test1',
        method: 'get'
    })
}
