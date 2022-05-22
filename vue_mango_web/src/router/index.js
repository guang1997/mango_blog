import Vue from 'vue'
import Router from 'vue-router'
import Meta from 'vue-meta'
const miss = () => import('@/components/base/miss')
const home = () => import('@/views/home/')
const blogDetail = () => import('@/views/blog/blogDetail')
const messageBoard = () => import('@/views/messageBoard/')
const archive = () => import('@/views/archive/')
const tag = () => import('@/views/tag/')
const blogFilter = () => import('@/views/blog-filter/')
const sort = () => import('@/views/sort/')
Vue.use(Router)
Vue.use(Meta)
// 避免重复点击相同路由 报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err)
}
const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '*',
      name: 'miss',
      component: miss
    },
    {
      path: '/',
      name: 'home',
      component: home
    },
    {
      path: '/app/blog/:id',
      name: 'blogDetail',
      component: blogDetail
    },
    {
      path: '/app/messageBoard',
      name: 'messageBoard',
      component: messageBoard
    },
    {
      path: '/app/archive',
      name: 'archive',
      component: archive
    },
    {
      path: '/app/tag',
      name: 'tag',
      component: tag
    },
    {
      path: '/app/sort',
      name: 'sort',
      component: sort
    },
    {
      path: '/app/blogs/:type/:param',
      name: 'blogFilter',
      component: blogFilter
    },
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
export default router
