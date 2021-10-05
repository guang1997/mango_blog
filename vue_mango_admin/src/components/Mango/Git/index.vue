<template>
  <div>
    <svg-icon class="giteeSvg" icon-class="gitee" @click.native="goto" style="height: 2.5em;font-size: 18px;cursor: pointer" />
  </div>
</template>

<script>
export default {
  name: 'MangoGit',
  data() {
    return {
      url: 'https://gitee.com/lisite1997/mango_blog'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
<style scoped>

</style>
