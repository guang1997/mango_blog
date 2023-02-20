<template>
  <div class="tags">
    <layout _title="标签" cover="/static/img/cover/tags.jpg">
      <timeline-iterator :activities="tags" :itemByDate="itemByDate" @getBlogList="getBlogList" :selectBlogId="selectBlogId" :displayName="displayName"></timeline-iterator>
    </layout>
  </div>
</template>
<script>
import blogApi from '@/api/blog'
import tagApi from '@/api/tag'
import timelineIterator from '@/views/components/timeline-iterator'
export default {
  name: 'tags',
  metaInfo() {
    return {
      title: `标签  - Lisite's Blog`
    }
  },
  components: { timelineIterator },
  data() {
    return {
      itemByDate:[],
      tags: [],
      selectBlogId:"",
      displayName:"标签"
    }
  },
   created() {
    tagApi.getTagByPage({ queryAll: true }).then(response => {
      if (response.code == 20000) {
        var result = []

        for (var a = 0; a < response.data.data.length; a++) {
          var dataForDate = {
            name: response.data.data[a].tagName,
            id: response.data.data[a].id
          };
          result.push(dataForDate);
        }
        
        this.tags = result;

        // 默认选择第一个
        this.getBlogList(this.tags[0].id);
      }
    });

  },
  methods: {
     getBlogList(tagId) {
      this.selectBlogId = tagId;
      blogApi.getBlogByTagId({tagId}).then(response => {
        if (response.code == 20000) {
          this.itemByDate = response.data;
        }
      });
    },
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
</style>
