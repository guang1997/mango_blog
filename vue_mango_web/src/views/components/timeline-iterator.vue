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
              <el-card class="article-iterator__item" >
                    <div class="item-content">
                      <div class="item-content__pic">
                        <img :src="item.fileId" alt="" />
                      </div>
                      <div class="item-content__info">
                        <div class="item-content__link">
                          <router-link class="live2d_router_link" :to="'/blog/' + item.id">{{ item.title }}</router-link>
                        </div>
                        <div class="item-content__detail">
                          <!-- <span>
                            <i class="el-icon-date"></i>
                            发表时间 {{ item.createTime }}
                          </span>
                          <span>&nbsp;|&nbsp;</span> -->
                          <span>
                            <i class="el-icon-chat-dot-round"></i>
                            评论数 {{ item.comments ? item.comments.length : 0 }}
                          </span>
                          <span>&nbsp;|&nbsp;</span>
                          <span>
                            <i class="el-icon-star-off"></i>
                            点赞 {{ item.likeCount }}
                          </span>
                        </div>
                        <div class="item-content__abstract">{{ item.summary }}</div>
                      </div>
                    </div>
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
@import '~@/style/index.scss';
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
.article-iterator {
  &__item {
    height: 280px;
    @include respond-to(xs) {
      height: auto;
    }
    > .el-card__body {
      width: 100%;
      height: 100%;
      padding: 0;
    }
    .item-content {
      display: flex;
      width: 100%;
      height: 100%;
      @include respond-to(xs) {
        flex-direction: column;
      }
      &__pic {
        width: 45%;
        height: 100%;
        @include respond-to(xs) {
          width: 100%;
          height: 230px;
        }
        flex: 0 0 auto;
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
      &__info {
        width: 55%;
        padding: 20px 30px;
        flex: 0 0 auto;
        display: flex;
        flex-direction: column;
        justify-content: center;
        @include respond-to(xs) {
          width: 100%;
          justify-content: normal;
          padding: 16px;
        }
      }
      &__link {
        flex: 0 0 auto;
        @include clamp(2);
        a {
          font-size: 24px;
          line-height: 1.5;
          transition: all ease-in-out 0.25s;
          @include respond-to(xs) {
            font-size: 18px;
          }
        }
        a:hover {
          @include themeify() {
            color: themed('color-ele-primary');
          }
        }
      }
      &__detail {
        @include themeify() {
          color: themed('color-home-article-detail');
        }
        font-size: 12px;
        padding: 12px 0;
      }
      &__abstract {
        line-height: 2;
        flex: 0 0 auto;
        @include clamp(2);
      }
    }
  }
  &__item:not(:first-child) {
    margin-top: 20px;
  }
  &__item:nth-child(even) .item-content__pic {
    order: 1;
    @include respond-to(xs) {
      order: 0;
    }
  }
}
</style>