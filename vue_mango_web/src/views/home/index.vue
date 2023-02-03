<template>
  <div class="home-article">
    <layout cover="/static/img/cover/home.jpg">
      <div id="home-article-header" class="home-article__header" slot="header">
        <div class="home-article__dictum">
          <div class="home-article__site-name">{{ webConfig.name }}</div>
          <div class="home-article__dictum-info">
            <span>{{ dictumInfo }}</span>
            <span class="home-article__typed-cursor" :class="{ 'is-typed-cursor-anmation': watingTyped }">|</span>
          </div>
        </div>
        <div class="home-article__go" @click="go">
          <i class="el-icon-arrow-down"></i>
        </div>
      </div>
      <div class="home-article__body" slot="custom-body">
        <blog-iterator :blogs="blogs"></blog-iterator>

        <div class="home-article__page">
          <el-pagination
            v-if="blogs.length"
            :total="total"
            layout="prev, pager, next"
            :page-size="pageSize"
            @current-change="currentChange"
          ></el-pagination>
        </div>
      </div>
    </layout>
  </div>
</template>

<script>
// 导入工具/组件
import scrollTo from '@/utils/scrollTo'
import blogApi from '@/api/blog'
import blogIterator from '@/views/components/blog-iterator'
import { mapState } from "vuex";
export default {
  // 组件名称
  name: 'home',
  // 子组件
  components: {
    blogIterator
  },
  metaInfo() {
    return {
      title: `首页  - Lisite's Blog`
    }
  },
  props: {},
  data() {
    return {
      total: 0, //
      pageSize: 5,
      dictumInfo: '',
      timer: null,
      backTimer: null,
      watingTyped: false,
      hidePage: false,
      blogs: [],
      // dictums: [
      //   ['小鱼干是换不来幸福的，因为它本身就是幸福'],
      //   ['有些事本来很遥远，你争取，它就会离你愈来愈近'],
      // ]
    }
  },
  created() {
    this.asyncData()
    
  },
  computed: {
    ...mapState(["webConfig"]),
  },
  watch: {},
  async mounted() {
    this.startPlay()
  },
  methods: {
    randArr(arr){
    return arr.sort(()=>{
        return (Math.random()-0.5);
    })
    }, 
    async asyncData() {
      const res = await blogApi.getBlogByPage({
        page: 1,
        size: 10
      })
      if(res.code === 20000) {
        this.blogs = res.data.data
        this.total = res.data.total
      }
    },
    async currentChange(val) {
      const res = await blogApi.getBlogByPage({
        page: val,
        size: this.pageSize
      })
      if (res.code === this.$ECode.SUCCESS) {
        this.total = res.data.total
        this.blogs = res.data.data
      }
    },

    go() {
      const height = document.querySelector('#home-article-header').clientHeight
      scrollTo(height)
    },
    async startPlay() {
      const dictums = this.randArr(this.webConfig.rollingSentences.flat())
      const tasks = dictums.map((dictum) => {
        return this.createTask(async (resolve) => {
          let i = 0
          this.timer = setInterval(async () => {
            this.dictumInfo = dictum.substring(0, i + 1)
            i++
            if (i >= dictum.length) {
              if (this.timer) {
                clearInterval(this.timer)
                this.watingTyped = true
                await this.sleep(800)
                this.watingTyped = false
                this.backTimer = setInterval(async () => {
                  this.dictumInfo = dictum.substring(0, i)
                  i--
                  if (i < 0) {
                    this.watingTyped = true
                    await this.sleep(200)
                    this.watingTyped = false
                    resolve()
                    if (this.backTimer) clearInterval(this.backTimer)
                  }
                }, 100)
              }
            }
          }, 250)
        })
      })
      await tasks.reduce((pre, next) => pre.then((ret) => next(ret)), Promise.resolve())
      this.startPlay()
    },
    createTask(cb) {
      return () =>
        new Promise((resolve) => {
          cb(resolve)
        })
    },
    sleep(delay = 500) {
      return new Promise((resolve) => {
        setTimeout(resolve, delay)
      })
    }
  },
  destroyed() {
    if (this.timer) clearInterval(this.timer)
    if (this.backTimer) clearInterval(this.backTimer)
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import '~@/style/index.scss';
.home-article {
  &__header {
    width: 100vw;
    height: 100vh;
    background-image: url('/static/img/cover/home.jpg');
    background-position: center;
    background-size: cover;
    position: relative;
    position: relative;
  }
  &__header:before {
    position: absolute;
    content: '';
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.4);
  }
  &__dictum {
    display: flex;
    flex-direction: column;
    align-items: center;
    z-index: 10;
    width: 100%;
    position: absolute;
    top: 45%;
    transform: translateY(-20px);
    @include themeify() {
      color: themed('color-navbar');
    }
  }
  &__dictum-info {
    text-align: center;
    font-size: 22px;
    margin-top: 24px;
    padding: 0 16px;
    line-height: 1.5;
    @include respond-to(xs) {
      margin-top: 14px;
      font-size: 18px;
      line-height: 1.5;
    }
  }
  &__typed-cursor {
    margin-left: 4px;
    font-size: 28px;
    @include respond-to(xs) {
      font-size: 18px;
    }
  }
  .is-typed-cursor-anmation {
    animation: typed 0.5s ease infinite alternate;
  }
  &__site-name {
    font-size: 36px;
    @include respond-to(xs) {
      font-size: 28px;
    }
  }
  &__go {
    position: absolute;
    bottom: 0;
    padding: 8px 0;
    text-align: center;
    width: 100%;
    cursor: pointer;
    .el-icon-arrow-down {
      animation: dance 1.5s ease-in infinite alternate;
      font-weight: 900;
      font-size: 24px;
      @include themeify() {
        color: themed('color-navbar');
      }
    }
  }
  &__body {
    // padding: 40px 0px;
  }

  &__page {
    padding: 16px 0;
    @include flex-box-center;
  }
}
@keyframes dance {
  from {
    opacity: 0.6;
    transform: translateY(0);
  }
  to {
    transform: translateY(-20px);
  }
}
@keyframes typed {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
</style>
