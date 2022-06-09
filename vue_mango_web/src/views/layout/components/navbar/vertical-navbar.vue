<template>
  <div class="vertical-navbar">
    <div class="vertical-navbar__intro">
      <site-introduction></site-introduction>
    </div>
    <split-line></split-line>
    <el-menu :default-active="activeIndex" class="el-menu-demo" @select="handleSelect">
      <el-menu-item index="home">
        <i class="el-icon-s-home"></i>
        <span>首页</span>
      </el-menu-item>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-discover"></i>
          <span>导航</span>
        </template>
        <el-menu-item index="archive">
          <i class="el-icon-files"></i>
          <span>归档</span>
        </el-menu-item>
        <el-menu-item index="tag">
          <i class="el-icon-collection-tag"></i>
          <span>标签</span>
        </el-menu-item>
        <el-menu-item index="sort">
          <i class="el-icon-folder-opened"></i>
          <span>分类</span>
        </el-menu-item>
      </el-submenu>
      <!-- <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-camera"></i>
          <span>娱乐</span>
        </template>
        <el-menu-item index="movies">
          <i class="el-icon-video-camera-solid"></i>
          <span>电影</span>
        </el-menu-item>
      </el-submenu> -->
      <el-menu-item index="messageBoard">
        <i class="el-icon-chat-dot-round"></i>
        <span>留言板</span>
      </el-menu-item>
      <el-menu-item index="friendLink">
        <i class="el-icon-ship"></i>
        <span>友链</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script>
import siteIntroduction from '@/views/components/site-introduction'
import splitLine from '@/components/splitLine/'
export default {
  data() {
    return {}
  },
  components: {
    splitLine,
    siteIntroduction
  },
  computed: {
    activeIndex() {
      return this.$route.name
    }
  },
  methods: {
    handleSelect(val) {
      this.$emit('goTo', val)
    }
  }
}
</script>
<style lang="scss">
.vertical-navbar {
  &__intro {
    padding: 14px;
  }
}
</style>
