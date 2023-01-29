<template>
  <div class="prev-next">
    <div class="prev-next__content">
      <div class="content__prev" v-if="prevBlog">
        <a :href="getHref('prev')">
          <img :src="prevBlog.fileId" alt="" />
          <div class="content__info">
            <div>上一篇</div>
            <div class="info-title">{{ this.prevBlog.title }}</div>
          </div>
        </a>
      </div>
      <div class="content__next" v-if="nextBlog">
        <a :href="getHref('next')">
          <img :src="nextBlog.fileId" alt="" />
          <div class="content__info">
            <div>下一篇</div>
            <div class="info-title">{{ this.nextBlog.title }}</div>
          </div>
        </a>
      </div>
    </div>
  </div>
</template>
<script>
import blogApi from "@/api/blog";
export default {
  name: 'prevnext',
  props: {
    createTime: {
      type: String
    }
  },
  data() {
    return {
      prevBlog: null,
      nextBlog: null
    }
  },
  created() {
    
  },
  components: {},
  methods: {
    async getPrevNextBlog() {
      const prevnextRes = await blogApi.getPrevNextBlog({
        createTime: this.createTime
      })
      if (prevnextRes.code === 20000) {
        this.prevBlog = prevnextRes.data.prevBlog
        this.nextBlog = prevnextRes.data.nextBlog
      }
    },
    getHref(type) {
      if (type === 'prev') return `/blog/${this.prevBlog.id}`
      else return `/blog/${this.nextBlog.id}`
    }
  },
  watch: {
     createTime: {
       handler (newValue, oldValue) {
         // 监听最新值，获取到值后再去查询数据
         this.getPrevNextBlog()  
       }
     }
    },

}
</script>
<style lang="scss">
@import '~@/style/index.scss';
.prev-next {
  &__content {
    position: relative;
    height: 150px;
    @include respond-to(xs) {
      height: 100px;
    }
    @include flex-box-space;
    .content__prev,
    .content__next {
      width: 50%;
      height: 100%;
      flex-grow: 1;
      a {
        position: relative;
        display: inline-block;
        width: 100%;
        height: 100%;
        overflow: hidden;
      }
      a:before {
        content: '';
        position: absolute;
        z-index: 5;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.4);
        transition: all ease-in 0.38s;
      }
      img {
        position: absolute;
        z-index: 0;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: all ease 0.38s;
      }
      a:hover:before {
        background: rgba(0, 0, 0, 0.2);
      }
      a:hover img {
        transform: scale(1.2);
      }
    }
    .content__prev .content__info {
      text-align: left;
    }
    .content__next .content__info {
      text-align: right;
    }
    .content__info {
      width: 100%;
      height: 100%;
      position: absolute;
      z-index: 10;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding: 0 38px;
      .info-title {
        @include clamp(2);
      }

      @include respond-to(xs) {
        padding: 0 12px;
      }
      @include themeify() {
        color: themed('color-title');
      }
    }
  }
}
</style>
