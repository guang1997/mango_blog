<template>
  <div>
    <svg-icon class="websiteSvg" icon-class="website" @click.native="goto" style="height: 2.5em;font-size: 18px;cursor: pointer" />
  </div>
</template>

<script>
export default {
  name: 'WebSite',
  data() {
    return {
      url: process.env.VUE_BLOG_WEB_URL
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
