<template>
  <div class="tags">
    <layout _title="标签" cover="/static/img/cover/tags.jpg">
      <tags-iterator :tags="tags"></tags-iterator>
    </layout>
  </div>
</template>
<script>
import tagApi from '@/api/tag'
import tagsIterator from '@/views/components/tags-iterator'
export default {
  name: 'tags',
  metaInfo() {
    return {
      title: `标签  - Lisite's Blog`
    }
  },
  components: { tagsIterator },
  data() {
    return {
      tags: []
    }
  },
  async asyncData() {
    const res = await tagApi.getTagByPage({})
    if (res.code === 20000) return { tags: res.data.data, total: res.total }
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
</style>
