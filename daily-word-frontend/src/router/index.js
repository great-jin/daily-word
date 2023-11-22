import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/index.vue'
import MainView from '@/views/home/index.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  }, {
    path: '/home',
    name: 'main',
    component: MainView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
