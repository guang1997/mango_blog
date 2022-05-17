import Vue from 'vue'
import Router from 'vue-router'
import Meta from 'vue-meta'

const home = () => import('@/views/home/')

Vue.use(Router)
Vue.use(Meta)
// 避免重复点击相同路由 报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err)
}

export function createRouter() {
  return new Router({
    mode: 'history',
    routes: [
      {
        path: '/',
        name: 'home',
        component: home
      }
    ],
    scrollBehavior(to, from, savedPosition) {
      if (to.hash || to.query.anchor) return false
      if (savedPosition) {
        return savedPosition
      } else {
        return { x: 0, y: 0 }
      }
    }
  })
}
