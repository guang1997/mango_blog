<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
import blogApi from '@/api/blog'
import tagApi from '@/api/tag'
import sortApi from '@/api/sort'
import commentApi from '@/api/comment'
export default {
  name: 'App',
  data() {
    return {
      location: [],
      timer: '',
      showBackTop: true
    }
  },
  mounted() {
    this.initPannel()
  },
  methods: {
    ...mapMutations(['setArchives', 'setSort', 'setTags', 'setNewComments', 'setNewBlogs', 'setTotals']),
    initPannel() {
      // this.$api.getArchives({ countType: 'month', page: 1, limit: 1000 }).then((res) => {
      //   if (res.status === 20000) this.setArchives(res.data)
      // })
      sortApi.getSortByPage({
        page: 1,
        size: 5
      }).then((res) => {
        if (res.status === 20000) {
          this.setTotals({ key: 'sort', value: res.total })
          this.setSort(res.data)
        }
      })
      tagApi.getTagByPage({
        page: 1,
        size: 1000
      }).then((res) => {
        if (res.status === 20000) {
          this.setTotals({ key: 'tag', value: res.total })
          this.setTags(res.data)
        }
      })
      commentApi
        .getCommentByPage({
          page: 1,
          size: 5
        })
        .then((res) => {
          if (res.status === 20000) this.setNewComments(res.data)
        })
      blogApi
        .getBlogByPage({
          page: 1,
          size: 5
        })
        .then((res) => {
          if (res.status === 20000) {
            this.setTotals({ key: 'blog', value: res.total })
            this.setNewBlogs(res.data)
          }
        })
    }
  }
}
</script>

<style lang="scss">
@import '~@/style/reset.scss';
@import './assets/css/prism.css';
@import './assets/css/emoji-sprite.css';
</style>
