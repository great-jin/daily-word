import axios from 'axios';
import {ElNotification} from 'element-plus';
import router from '@/router/index'
import {getToken, clearToken} from "@/util/AuthUtil";

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
        const code = res.data.code
        if (res.status === 203 || code === 403) {
            ElNotification({
                title: '登录过期',
                message: '登录过期, 请重新登录',
                type: 'warning'
            })
            clearToken()
            router.push('/')
        }
        if (axiosConfig.url === '/api/auth/login') {
            // 登录页需返回请求头
            return res
        } else {
            return res.data
        }
    }, err => {
        // 请求信息弹窗提示
        const errorBody = err.response.data
        ElNotification({
            title: 'Internal Server Error',
            message: errorBody,
            type: 'error'
        })
    })

    return service(axiosConfig)
}

export default request;