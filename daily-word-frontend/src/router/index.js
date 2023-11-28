import { createRouter, createWebHistory } from 'vue-router'
import {pathArray} from "@/router/component";

const routes = pathArray

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  // 存入的字符串转对象
  const token = JSON.parse(localStorage.getItem('token'))
  // 状态判断
  const isLogin = !(token == null || token.flag == null || token.flag === false)
  // 是否为主页或登录页
  if(to.path === '/' || to.path === '/login') {
    // 已登录则回首页，未登录放行
    isLogin ? next('/home') : next()
  } else {
    // 已登录则放行，未登录转登录页
    isLogin ? next() : next('/')
  }
})
export default router
