import Vue from 'vue'
import Router from 'vue-router'
import Meta from 'vue-meta'
const miss = () => import('@/components/base/404')
const home = () => import('@/views/home/')
const blogDetail = () => import('@/views/blog/blogDetail/')
const messageBoard = () => import('@/views/messageBoard/')
const archive = () => import('@/views/archive/')
const tag = () => import('@/views/tag/')
const blogFilter = () => import('@/views/blog-filter/')
const sort = () => import('@/views/sort/')
const friendLink = () => import('@/views/friendLink/')
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
      path: '/blog/:id',
      name: 'blogDetail',
      component: blogDetail
    },
    {
      path: '/messageBoard',
      name: 'messageBoard',
      component: messageBoard
    },
    {
      path: '/archive',
      name: 'archive',
      component: archive
    },
    {
      path: '/tag',
      name: 'tag',
      component: tag
    },
    {
      path: '/sort',
      name: 'sort',
      component: sort
    },
    {
      path: '/blog/:type/:param',
      name: 'blogFilter',
      component: blogFilter
    },
    {
      path: '/friendLink',
      name: 'friendLink',
      component: friendLink
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
