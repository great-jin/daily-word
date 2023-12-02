import Vue from 'vue';
import axios from 'axios';

function request(axiosConfig) {
    const service = axios.create({
        baseURL: '/daily-word',        // 设置统一的请求前缀
        timeout: 60000,         // 设置统一的超时时长, 60s
    });

    service.interceptors.request.use(config => {
        // 请求添加默认请求头
        const token = getToken()[0]
        if (token !== '') {
            config.headers['Token'] = token
        }
        const auth = getToken()[1]
        if (auth !== '') {
            config.headers['Authorization'] = auth
        }
        return config
    }, err => {
        console.log(err);
    })

    // 响应拦截
    service.interceptors.response.use(res => {
        if (axiosConfig.url === '/api/auth/login') {
            // 登录页需返回请求头
            return res
        } else {
            return res.data
        }
    }, err => {
        // 请求信息弹窗提示
        const errorBody = err.response.data
        Vue.prototype.$notify.error({
            message: 'Internal Server Error',
            description: errorBody.error,
        });
    })

    return service(axiosConfig)
}

export default request;

function getToken() {
    let token = localStorage.getItem('token')
    if (token === undefined || token === null) {
        token = ''
    }
    let auth = localStorage.getItem('auth')
    if (auth === undefined || auth === null) {
        auth = ''
    }
    return [token, auth]
}