import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control
// 添加粒子特效
import VueParticles from 'vue-particles'

// 权限指令
import checkPer from '@/utils/permission'
import permission from '@/directive/Permission'

// 数据字典
import dict from '@/components/Dict'

// 全局工具类
import prototype from '@/utils/prototype'

// 富文本编辑器
import CKEditor from 'ckeditor4-vue';

Vue.use(checkPer)
Vue.use(VueParticles)
Vue.use(prototype)
Vue.use(dict)
Vue.use(permission)
// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
Vue.use(ElementUI)

Vue.use(CKEditor)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
