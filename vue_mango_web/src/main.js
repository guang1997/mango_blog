import Vue from 'vue'
import App from './App'
import empty from '@/components/empty'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import layout from '@/views/layout/'
import router from './router'
import store  from './store'
import mergeAsyncData from '@/mixins/mergeAsyncData'
import R_O_P from 'resize-observer-polyfill'
import VueLazyload from '@/utils/lazyLoad'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'
const loading = require('@/assets/img/loading.gif')
if (!window.ResizeObserver) {
  window.ResizeObserver = R_O_P
}

Vue.mixin(mergeAsyncData)
Vue.use(ElementUI)
// eslint-disable-next-line no-undef
Vue.use(VueLazyload, {
  loading: loading
})

Vue.component('empty', empty)
Vue.component('layout', layout)

Vue.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})
Vue.config.productionTip = false
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})