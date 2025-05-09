import axios from 'axios';
import {ElNotification} from 'element-plus';
import router from '@/router/index'
import {setRequestHeader, clearToken} from "@/util/AuthUtil";

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
        return setRequestHeader(config)
    }, err => {
        console.log('request error', err);
    })

    // 响应拦截
    service.interceptors.response.use(res => {
        if (res === null || res.data == null) {
            noticeAnClear()
            router.push('/')
            return null
        }
        const code = res.data.code
        if (code !== undefined && code !== null) {
            // 未登录返回
            if (code === 203 || code === 401 || code === 403) {
                noticeAnClear()
                router.push('/')
                return res.data
            }

            // 失败提示
            if (code === 500) {
                ElNotification({
                    title: '操作失败',
                    message: res.data.message,
                    type: 'error'
                })
                return res.data
            }
        }

        return res.data
    }, err => {
        // 请求信息弹窗提示
        let message = err.response
        if (message !== undefined && message !== null) {
            message = message.data.message
        }
        ElNotification({
            title: '操作失败',
            message: message,
            type: 'error'
        })
    })

    return service(axiosConfig)
}

export default request;

function noticeAnClear() {
    ElNotification({
        title: '登录过期',
        message: '登录过期, 请重新登录',
        type: 'warning'
    })
    clearToken()
}