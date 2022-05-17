// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import empty from '@/components/empty'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import layout from '@/views/layout/'
import { createRouter } from './router'
import { createStore } from './store'
import { sync } from 'vuex-router-sync'
import mergeAsyncData from '@/mixins/mergeAsyncData'

Vue.mixin(mergeAsyncData)
Vue.use(ElementUI)
Vue.component('empty', empty)
Vue.component('layout', layout)

Vue.config.productionTip = false

export function createApp() {
  const router = createRouter()
  const store = createStore()
  sync(store, router)
  const app = new Vue({
    router,
    store,
    render: (h) => h(App)
  })

  return { app, router, store }
}

/* eslint-disable no-new */
// new Vue({
//   el: '#app',
//   router,
//   store,
//   components: { App },
//   template: '<App/>'
// })
