import {createRouter, createWebHashHistory} from 'vue-router'
import {pathArray} from "@/router/component";
import {getToken} from "@/util/AuthUtil";
import {ElNotification} from "element-plus";

const routes = pathArray

const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
})

const WHITE_LIST = [
    '/',
    '/logout',
    '/dictionary'
]

router.beforeEach((to, from, next) => {
    if (WHITE_LIST.includes(to.path)) {
        next()
        return
    }

    // 状态判断
    const loginInfo = getToken()
    const containAuth = !(loginInfo[0] == null || loginInfo[0] === '')
    const containToken = !(loginInfo[1] == null || loginInfo[1] === '')
    const isLogin = containAuth && containToken
    if (to.path === '/login') {
        // 已登录则回主页，未登录放行
        isLogin ? next('/') : next()
        return
    }

    // 除首页与登录页外未登录不允许访问
    if (isLogin) {
        next()
    } else {
        ElNotification({
            title: '尚未登录',
            message: '此功能需登录后访问',
            type: 'warning'
        })
        next('/login')
    }
})
export default router
