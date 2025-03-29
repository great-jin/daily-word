import axios from 'axios';
import {ElNotification} from 'element-plus';
import router from '@/router/index'
import {getToken, clearToken} from "@/util/AuthUtil";

function request(axiosConfig) {
    const service = axios.create(
        {
            // 统一的请求前缀
            baseURL: `${process.env.VUE_APP_API_BASE_PREFIX}`,
            // 统一的超时时长, 60s
            timeout: 60_000,
        }
    );

    service.interceptors.request.use(config => {
        // 请求添加默认请求头
        const token = getToken()
        if (token[0] !== '') {
            config.headers['Token'] = token[0]
        }
        if (token[1] !== '') {
            config.headers['Authorization'] = token[1]
        }
        return config
    }, err => {
        console.log('request error', err);
    })

    // 响应拦截
    service.interceptors.response.use(res => {
        const code = res.data.code
        if (code !== undefined && code !== null && (code === 203 || code === 403)) {
            ElNotification({
                title: '登录过期',
                message: '登录过期, 请重新登录',
                type: 'warning'
            })
            clearToken()
            router.push('/')
            return null
        }

        return res.data
    }, err => {
        // 请求信息弹窗提示
        const errorBody = err.response.data
        ElNotification({
            title: 'Internal Server Error',
            message: errorBody.message,
            type: 'error'
        })
    })

    return service(axiosConfig)
}

export default request;