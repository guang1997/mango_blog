<template>
  <div class="site-introduction">
    <div class="site-introduction__avatar" @click="goTo('home')">
      <img src="https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/15/09fb67b0-f1d2-4d63-afda-a72219d55c65-avatar.jpeg" alt="" />
    </div>
    <div class="site-introduction__info" @click="goTo('home')">
      <p class="site-introduction__sitename">{{ webConfig.author }}</p>
      <p v-html="webConfig.summary">
      </p>
    </div>
    <div class="site-introduction__totals">
      <div class="totals__item" @click="goTo('sort')">
        <div>文章</div>
        <div class="totals__number">{{ totals.blog }}</div>
      </div>
      <div class="totals__item" @click="goTo('sort')">
        <div>分类</div>
        <div class="totals__number">{{ totals.sort }}</div>
      </div>
      <div class="totals__item" @click="goTo('tag')">
        <div>标签</div>
        <div class="totals__number">{{ totals.tag }}</div>
      </div>
    </div>
    <div class="site-introduction__social">
       <div class="social-github">
        <a :href="webConfig.github" target="_blank">
          <el-button type="primary" size="mini">
            <div class="social-github__icon">
              <svg
                height="18"
                aria-hidden="true"
                viewBox="0 0 16 16"
                version="1.1"
                width="18"
                data-view-component="true"
                class="octicon octicon-mark-github v-align-middle"
              >
                <path
                  fill-rule="evenodd"
                  d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"
                ></path>
              </svg>
              <span>GitHub</span>
            </div>
          </el-button>
        </a>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import webConfig from '../../api/webConfig'

export default {
  name: 'pannelIntroduction',
  inject:['reload'],
  props: {},
  data() {
    return {}
  },
  computed: {
    ...mapState(['totals', 'webConfig'])
  },
  methods: {
    goTo(name) {
      this.$router.push({ name })
      this.reload()
    }
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
@import '~@/views/pannel/style/mixins';

.site-introduction {
  &__avatar {
    width: 80px;
    height: 80px;
    margin: 0 auto;
    cursor: pointer;
    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      transition: all ease 0.38s;
    }
    img:hover {
      transform: rotate(360deg);
    }
  }
  &__info {
    margin-top: 16px;
    text-align: center;
    line-height: 2;
    color: #4c4948;
    a {
      @include themeify() {
        color: themed('color-ele-primary');
      }
    }
  }
  &__sitename {
    font-size: 16px;
    font-weight: 600;
  }
  &__totals {
    margin-top: 16px;
    text-align: center;
    display: flex;
    justify-content: space-around;

    .totals__item {
      cursor: pointer;
    }
    .totals__number {
      font-size: 16px;
      font-weight: 600;
    }
  }
  &__social {
    margin-top: 16px;

    .el-button {
      width: 100%;
      > span {
        width: 100%;
        display: inline-block;
      }
    }
    .social-github__icon {
      display: inline-block;

      display: flex;
      align-items: center;
      justify-content: center;
      svg {
        display: inline-block;
        width: 16px;
        height: 16px;
        flex: 0 0 auto;
        margin-right: 8px;
      }
    }
  }
}
</style>
