<template>
  <div class="category">
    <layout _title="分类" cover="/static/img/cover/category.jpg">
      <timeline-iterator :activities="sorts" :itemByDate="itemByDate" @getBlogList="getBlogList" :selectBlogId="selectBlogId" :displayName="displayName"></timeline-iterator>
    </layout>
  </div>
</template>
<script>
import blogApi from '@/api/blog'
import sortApi from '@/api/sort'
import timelineIterator from '@/views/components/timeline-iterator'
export default {
  name: 'category',
  metaInfo() {
    return {
      title: `文章分类  - Lisite's Blog`
    }
  },
   created() {
    sortApi.getSortByPage({ queryAll: true }).then(response => {
      if (response.code == 20000) {
        var result = []

        for (var a = 0; a < response.data.data.length; a++) {
          var dataForDate = {
            name: response.data.data[a].sortName,
            id: response.data.data[a].id
          };
          result.push(dataForDate);
        }
        
        this.sorts = result;
        
        // 默认选择第一个
        this.getBlogList(this.sorts[0].id);
      }
    });

  },
  components: {
    timelineIterator
  },
  data() {
    return {
      sorts: [],
      selectBlogId: "",
       itemByDate: [],
       displayName:"分类"
    }
  },
  methods: {
     getBlogList(blogSortId) {
      this.selectBlogId = blogSortId;
      blogApi.getBlogBySortId({blogSortId}).then(response => {
        if (response.code == 20000) {
           var result = []

          for (var a = 0; a < response.data.data.length; a++) {
            var dataForDate = {
              id: response.data.data[a].id,
              title: response.data.data[a].title,
              createTime: response.data.data[a].createTime,
              author: response.data.data[a].author,
              tags: response.data.data[a].tags
            };
            result.push(dataForDate);
          }
          this.itemByDate = result;
        }
      });
    },
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
.category {
  &__content {
    li {
      padding: 6px 0;
      transition: all ease 0.38s;
    }
    li:hover {
      cursor: pointer;
      @include themeify() {
        color: themed('color-ele-primary');
      }
    }
  }
}

</style>
