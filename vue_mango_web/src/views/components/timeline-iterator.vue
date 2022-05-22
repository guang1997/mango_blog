<template>
  <div class="timeline-iterator" v-if="activities.length">
    <el-row>
      <el-col :span="4">
        <div class="sortBox">
          <div class="time">
            <div class="block">
              <div class="radio" style="margin-bottom: 20px"></div>
              <el-timeline :reverse="reverse">
                <el-timeline-item
                  v-for="(activity, index) in activities"
                  hide-timestamp
                  :key="index"
                >
                  <span
                    @click="getBlogList(activity.id)"
                    :class="[
                      activity.id == selectBlogId
                        ? 'sortBoxSpan sortBoxSpanSelect'
                        : 'sortBoxSpan',
                    ]"
                    >{{ activity.name }}</span
                  >
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
                  <h4 @click="goToList('blogContent', item)" class="itemTitle">
                    {{ item.title }}
                  </h4>
                  <br />
                  <el-tag
                    class="elTag"
                    v-if="item.author"
                    @click="goToList('author', item)"
                    >{{ item.author }}</el-tag
                  >
                    <el-tag
                      class="elTag"
                      
                      v-for="tagItem in item.tags"
                      :key="tagItem.id"
                      style="margin-left: 3px"
                      @click="goToList('tag', tagItem)"
                      type="success"
                      >{{ tagItem.tagName }}</el-tag
                    >
                    <el-tag
                      class="elSort"
                      v-if="item.sort"
                      style="margin-left: 3px"
                      @click="goToList('tag', tagItem)"
                      type="warning"
                      >{{ item.sort.sortName }}</el-tag
                    >

                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <div v-else>
    <empty :text="'您还没有' + displayName + '，赶快去创建吧~'"></empty>
  </div>
</template>

<script>
export default {
  name: "timelineIterator",
  props: {
    activities: {
      type: Array,
      default: [],
    },
    itemByDate: {
      type: Array,
      default: [],
    },
    selectBlogId: {
      type: String,
    },
    displayName: {
      type: String,
    },
  },
  data() {
    return {
      reverse: false,
    };
  },
  methods: {
    getBlogList(id) {
      this.$emit("getBlogList", id);
  
    },
  },
};
</script>

<style lang="scss">
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
.elSort {
  cursor: pointer;
}
.el-card-define {
  min-height: 100%;
  height: 100%;
  padding: 10px, 10px;
}
</style>