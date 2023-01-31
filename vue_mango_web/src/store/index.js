import Vue from 'vue'
import Vuex from 'vuex'
import actions from './actions'
import mutations from './mutations'
import { storage } from "@/utils/storage";

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    activeCatalog: '',
    rollBack: false,
    // 访客信息
    visitorInfo: storage.getVisitor() ? storage.getVisitor() : {},
    // 文章目录树
    catalogs: [],
    // 文章归档 按月统计
    archives: [],
    // 文章分类
    sorts: [],
    // 文章标签
    tags: [],
    // 最新评论
    newComments: [],
    // 最新文章
    newBlogs: [],
    totals: {
      blog: 0,
      tag: 0,
      sort: 0
    },
    webConfig: {
      // name: "",
      // summary: "",
      // author: "",
      // recordNum: "",
      // github: "",
      // gitee: "",
      // rollingSentences: []
    }
  },
  mutations,
  actions
})

export default store
