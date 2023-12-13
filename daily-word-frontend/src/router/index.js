import {createRouter, createWebHashHistory} from 'vue-router'
import {pathArray} from "@/router/component";
import {getToken} from "@/util/AuthUtil";

const routes = pathArray

const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    // 首页访问不做拦截
    if (to.path === '/') {
        next()
    }

    // 状态判断
    const token = getToken()[0]
    const isLogin = !(token == null || token === '')
    // 是否访问登录页
    if (to.path === '/login') {
        // 已登录则回主页，未登录放行
        isLogin ? next('/home') : next()
    } else {
        // 已登录则放行，未登录转首页并提示
        if (isLogin) {
            next()
        } else {
            next('/')
        }
    }
})
export default router
