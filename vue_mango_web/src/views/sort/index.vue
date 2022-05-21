<template>
  <div class="category">
    <layout _title="分类" cover="/static/img/cover/category.jpg">
    <el-row>
      <el-col :span="4">
 <div class="sortBox">
        <div class="time">
          <div class="block">
            <div class="radio" style="margin-bottom:20px;"></div>
            <el-timeline :reverse="reverse">
              <el-timeline-item v-for="(activity, index) in activities" hide-timestamp :key="index">
                <span
                  @click="getBlogList(activity.id)"
                  :class="[activity.id == selectBlogId ? 'sortBoxSpan sortBoxSpanSelect' : 'sortBoxSpan']"
                >{{activity.sortName}}</span>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </div>
       </el-col>
       <el-col :span="20">
      <div class="article">
          <div class="block">
            <el-timeline>
              <el-timeline-item
                v-for="item in itemByDate"
                :key="item.id"
                :timestamp="item.createTime"
                placement="top"
              >
                <el-card class="el-card-define">
                  <h4 @click="goToList('blogContent', item)" class="itemTitle">{{item.title}}</h4>
                  <br>
                  <el-tag
                    class="elTag"
                    v-if="item.author"
                    @click="goToList('author', item)"
                  >{{item.author}}</el-tag>

                  <el-tag
                    class="elTag"
                    v-for="tagItem in item.tagList"
                    v-if="tagItem != null"
                    :key="tagItem.uid"
                    style="margin-left: 3px;"
                    @click="goToList('tag', tagItem)"
                    type="warning"
                  >{{tagItem.content}}</el-tag>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
        </el-col>
      </el-row> 
    </layout>
  </div>
</template>
<script>
import blogApi from '@/api/blog'
import sortApi from '@/api/sort'
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
        this.activities = response.data.data;

        // 默认选择第一个
        this.getBlogList(this.activities[0].id);
      }
    });

  },
  data() {
    return {
      sorts: [],
      reverse: false,
      activities: [],
      selectBlogId: "",
       itemByDate: [],
       currentPage: 1,
      pageSize: 10
    }
  },
  // async asyncData() {
  //   const res = await sortApi.getSortByPage({})
  //   if (res.code === 20000) return { sorts: res.data.data }
  // },
  methods: {
     getBlogList(blogSortId) {
      this.selectBlogId = blogSortId;
      // var params = new URLSearchParams();
      // params.append("blogSortId", blogSortId);
      blogApi.getBlogBySortId({blogSortId}).then(response => {
        if (response.code == 20000) {
          this.itemByDate = response.data.data;
          this.currentPage = response.data.page;
          this.pageSize = response.data.size;
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
.sortBox {
  color: #555;
}
.sortBoxSpan {
  cursor: pointer;
}
.sortBoxSpan:hover {
  color: #409eff;
}
.sortBoxSpanSelect {
  color: #409eff;
}
.itemTitle {
  cursor: pointer;
}
.itemTitle:hover {
  color: #409eff;
}
.elTag {
  cursor: pointer;
}
.el-card-define {
  min-height: 100%;
  height: 100%;
  padding: 10px, 10px;
}
</style>
