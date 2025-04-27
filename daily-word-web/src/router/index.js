import {createRouter, createWebHashHistory} from 'vue-router'
import {pathArray} from "@/router/component";
import {getToken, isUseLogin} from "@/util/AuthUtil";
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

function notify(){
    ElNotification({
        title: '尚未登录',
        message: '此功能需登录后访问',
        type: 'warning'
    })
}

router.beforeEach((to, from, next) => {
    if (WHITE_LIST.includes(to.path)) {
        // 白名单放行
        next()
        return
    }

    // 状态判断
    const loginInfo = getToken()
    if (loginInfo === null || loginInfo.length === 0) {
        notify()
        next('/login')
        return
    }

    const isLogin = isUseLogin()
    if (to.path === '/login') {
        // 已登录用户访问登录页重定向首页
        isLogin ? next('/') : next()
        return
    }

    // 除首页与登录页外未登录不允许访问
    if (isLogin) {
        next()
    } else {
        notify()
        next('/login')
    }
})
export default router
