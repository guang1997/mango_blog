<template>
  <div class="article-filter">
    <layout :_title="title" cover="/static/img/cover/articles.jpeg">
      <template slot="custom-body">
        <blog-iterator :blogs="blogs"></blog-iterator>
        <div class="article-filter__page">
          <el-pagination
            :total="total"
            layout="prev, pager, next"
            :page-size="size"
            @current-change="currentChange"
          ></el-pagination>
        </div>
      </template>
    </layout>
  </div>
</template>
<script>
import blogApi from '@/api/blog'
import blogIterator from '@/views/components/blog-iterator'

// 筛选文章公共请求
async function fetchArticles(route, page = 1, size = 10) {
  const params = {
    page,
    size
  }
  // 按标签筛选
  if (route.params.type === 'tag') params.tagId = route.params.param
  // 按分类筛选
  if (route.params.type === 'sort') params.blogSortId = route.params.param

  const articleRes = await blogApi.getBlogByPage(params)
  return articleRes
}

export default {
  name: 'blogFilter',
  metaInfo() {
    return {
      title: `文章筛选：${this.title}  - Lisite's Blog`
    }
  },
  data() {
    return {
      size: 10,
      page: 1,
      blogs: [],
      total: 0
    }
  },
  created() {},
  components: { blogIterator },
  async asyncData({ route }) {
    const articleRes = await fetchArticles(route)
    if (articleRes.code === 20000) return { blogs: articleRes.data.data, total: articleRes.data.total }
  },
  computed: {
    title() {
      return this.$route.query.name
    }
  },
  methods: {
    currentChange(val) {
      this.page = val
      this.getArticles()
    },
    async getArticles() {
      const articleRes = await fetchArticles(this.$route,  this.page, this.size)
      if (articleRes.code === 20000) {
        this.blogs = articleRes.data.data
        this.total = articleRes.total
      }
    }
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
.article-filter {
  &__page {
    padding: 12px;
    @include flex-box-center;
  }
}
</style>
