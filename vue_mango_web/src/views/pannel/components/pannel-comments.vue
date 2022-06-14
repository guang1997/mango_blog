<template>
  <div class="pannel-comments">
    <el-card>
      <div class="pannel__item-content">
        <div class="pannel__item-title">
          <i class="el-icon-chat-dot-square"></i>
          <span>最新评论</span>
        </div>
        <div class="pannel__item-body">
          <template v-if="newComments.length">
            <div
              class="body-item"
              v-for="(comment, index) in newComments"
              :key="index"
            >
              <div class="body-pic">
                <div v-if="comment.blogId">
                  <router-link
                    :to="'/app/blog/' + comment.blogId + '?anchor=a_cm'"
                    @click.native="reloadBlog()"
                  >
                    <img v-lazy="comment.avatar" alt="" />
                  </router-link>
                </div>
                <div v-else>
                  <router-link
                    :to="'/app/messageBoard?anchor=a_cm'"
                    @click.native="reloadBlog()"
                  >
                    <img v-lazy="comment.avatar" alt="" />
                  </router-link>
                </div>
              </div>
              <div class="body-info">
                <div class="body-info__title">
                  <div v-if="comment.blogId">
                    <router-link
                      :to="'/app/blog/' + comment.blogId + '?anchor=a_cm'"
                      @click.native="reloadBlog()"
                    >
                      {{ comment.content }}
                    </router-link>
                  </div>
                  <div v-else>
                    <router-link
                      :to="'/app/messageBoard?anchor=a_cm'"
                      @click.native="reloadBlog()"
                    >
                      {{ comment.content }}
                    </router-link>
                  </div>
                </div>
                <div class="body-info__name">{{ comment.name }}</div>
                <div class="body-info__date">
                  发表于：{{ comment.createTime }}
                </div>
              </div>
            </div>
          </template>
          <empty v-else></empty>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import { mapState } from "vuex";

export default {
  name: "pannelComments",
  inject: ["reload"],
  props: {},
  data() {
    return {};
  },
  computed: {
    ...mapState(["newComments"]),
  },
  methods: {
    reloadBlog() {
      this.reload();
    },
  },
};
</script>
<style lang="scss">
@import "~@/style/index.scss";
@import "~@/views/pannel/style/mixins";

.pannel-comments {
  @include articles-comments;
  .body-info {
    &__title a {
      -webkit-line-clamp: 1 !important;
    }
    &__name {
      padding-top: 4px;
      @include clamp(1);
    }
    &__date {
      padding: 2px 0 !important;
    }
  }
}
</style>
