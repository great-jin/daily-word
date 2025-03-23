import {createRouter, createWebHashHistory} from 'vue-router'
import {pathArray} from "@/router/component";
import {getToken} from "@/util/AuthUtil";
import {ElNotification} from "element-plus";

const routes = pathArray

const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    if (to.path === '/' || to.path === '/logout' || to.path === '/dictionary') {
        // 免登录
        next()
        return
    }

    // 状态判断
    const auth = getToken()[1]
    const token = getToken()[0]
    const isAuth = !(auth == null || auth === '')
    const isLogin = isAuth && !(token == null || token === '')
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
