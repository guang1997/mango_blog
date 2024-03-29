<template>
  <div class="pannel">
    <pannel-introduction :style="{ width: stickyOffsetWidth }"></pannel-introduction>
    <div
      class="pannel__sticky"
      :class="{
        'pannel--sticky-top': sticky,
        'pannel--sticky-rollback': rollBack && !stickyBottom,
        'pannel--sticky-bottom': stickyBottom
      }"
      :style="{ width: stickyOffsetWidth }"
    >
      <component v-for="(pannel, index) in enums" class="pannel__item" :is="pannel" :key="index"></component>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import pannelIntroduction from './components/pannel-introduction'
import pannelCatalog from './components/pannel-catalog'
import pannelBlogs from './components/pannel-blogs'
import pannelComments from './components/pannel-comments'
import pannelSorts from './components/pannel-sorts'
import PannelTags from './components/pannel-tags.vue'
import pannelArchives from './components/pannel-archives'
import { getScrollTop } from '@/utils/getScrollTop'
import { getElementTop } from '@/utils/getElementTop'
import debounce from 'lodash/debounce'
export default {
  name: 'pannel',
  components: {
    pannelIntroduction,
    pannelCatalog,
    pannelBlogs,
    pannelComments,
    pannelSorts,
    PannelTags,
    pannelArchives
  },
  inject: {
    pannels: {
      from: 'pannels',
      default: () => ({})
    }
  },
  props: {},
  data() {
    return {
      sticky: false,
      stickyBottom: false,
      pannelOffsetTop: 0,
      stickyOffsetTop: 0,
      bodyPageWrapperTop: 0,
      pannelOffsetHeight: 0,
      stickyOffsetWidth: 'auto',
      stickyOffsetHeight: 0,
      bodyPageWrapperHeight: 0,
      observer: null
    }
  },
  mounted() {
    this.initStickybehavior()
  },
  watch: {
    $route(to, from) {
      //  锚点跳转防止routeUpdate
      if (JSON.stringify(to.params) !== JSON.stringify(from.params)) {
        this.removeEffect()
        this.initStickybehavior()
      }
    }
  },
  computed: {
    ...mapState(['rollBack']),
    enums() {
      // 路由进行自定义看板、顺序
      const def = [
        'pannel-catalog',
        'pannel-blogs',
        'pannel-comments',
        'pannel-tags',
        'pannel-sorts',
        'pannel-archives'
      ]
      if (this.pannels) {
        if (this.pannels.includes && Array.isArray(this.pannels.includes)) return this.pannels.includes
        if (this.pannels.excludes) {
          this.pannels.excludes.forEach((item) => {
            if (def.includes(item)) {
              const index = def.indexOf(item)
              if (index !== -1) def.splice(index, 1)
            }
          })
        }
      }
      if (this.$router.currentRoute.name !== 'blogDetail') {
        const index = def.indexOf('pannel-catalog')
        if (index !== -1) def.splice(index, 1)
      }
      return def
    }
  },
  methods: {
    initStickybehavior() {
      window.addEventListener('scroll', this.stickyHandler, false)
      window.addEventListener('resize', this.resizeHandler, false)
      const bodyPageWrapperNode = document.querySelector('.body-page__wrapper')
      const pannelNode = document.querySelector('.pannel')
      const stickNode = document.querySelector('.pannel__sticky')
      this.observer = new ResizeObserver(() => {
        if (document.documentElement.clientWidth <= 768) return
        this.pannelOffsetHeight = pannelNode.offsetHeight
        this.stickyOffsetHeight = stickNode.offsetHeight
        this.bodyPageWrapperHeight = bodyPageWrapperNode.offsetHeight

        // 尺寸变化后，主动触发一次计算，避免手动滚动页面时才进行重新布局
        this.stickyHandler()
      })
      this.observer.observe(pannelNode, { attributes: true, childList: false, subtree: false })
      this.observer.observe(stickNode, { attributes: true, childList: false, subtree: false })
      this.$nextTick(() => {
        this.stickyOffsetTop = getElementTop(stickNode)
        this.pannelOffsetTop = getElementTop(pannelNode)
        this.bodyPageWrapperTop = getElementTop(bodyPageWrapperNode)
        this.stickyOffsetHeight = stickNode.offsetHeight
        this.stickyOffsetWidth = stickNode.offsetWidth + 'px'
        this.pannelOffsetHeight = pannelNode.offsetHeight
        this.bodyPageWrapperHeight = bodyPageWrapperNode.offsetHeight
      })
    },
    stickyHandler() {
      // 视口尺寸小于等于768px，则默认手机状态布局，无需处理sticky逻辑
      if (document.documentElement.clientWidth <= 768) return
      if (this.stickyOffsetTop + this.stickyOffsetHeight > this.bodyPageWrapperTop + this.bodyPageWrapperHeight) {
        // 此时应该清空状态
        this.sticky = false
        this.stickyBottom = false
        return
      }
      const scrollTop = getScrollTop()
      const distance = this.rollBack ? 70 : 20

      if (this.stickyOffsetTop - scrollTop <= distance) {
        this.sticky = true
        this.stickyBottom = false
        // 当页面回滚，sticky容器将固定与top：70px位置，正常向下滚动，位于top：20px位置
        // 避免像素跳转，产生闪烁问题
        const threshold = this.rollBack ? 70 : 20

        if (this.pannelOffsetHeight + this.pannelOffsetTop - scrollTop - threshold <= this.stickyOffsetHeight) {
          this.sticky = false
          this.stickyBottom = true
        }
      } else {
        this.sticky = false
      }
    },
    resizeHandler: debounce(function () {
      const pannelNode = document.querySelector('.pannel')
      const layoutBody = document.querySelector('.layout__body-content')
      this.stickyOffsetWidth = pannelNode.offsetWidth + 'px'
      const maxWidth = window.getComputedStyle(layoutBody)['max-width']
      // 切换至手机尺寸后，将sticky相关状态全部置空
      if (maxWidth === '768px') {
        this.sticky = false
        this.stickyBottom = false
      }
    }, 200),
    removeEffect() {
      this.sticky = false
      this.stickyBottom = false
      this.observer.disconnect()
      window.removeEventListener('scroll', this.stickyHandler, false)
      window.removeEventListener('resize', this.resizeHandler, false)
    }
  },

  destroyed() {
    this.removeEffect()
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';

.pannel {
  position: relative;
  height: 100%;
  &__sticky {
    transition: all ease 0.38s;
  }
  &__item:not(:first-child) {
    margin-top: 16px;
  }
}
.pannel--sticky-top {
  position: fixed;
  top: 20px;
}
.pannel--sticky-bottom {
  position: absolute;
  bottom: 0;
}
.pannel--sticky-rollback {
  top: 70px;
}
@include respond-to(xs) {
  .pannel {
    margin-top: 16px;
  }
  .pannel__sticky {
    position: relative !important;
    top: 0 !important;
  }
}
</style>
