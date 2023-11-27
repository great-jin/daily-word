import { createRouter, createWebHistory } from 'vue-router'
import {pathArray} from "@/router/component";

const routes = pathArray

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
