<template>
  <div class="comments-item">
    <div class="comments-item__avatar" :class="{ 'comments-item__avatar--small': subType }">
      <!-- <a :href="comment.link" target="_blank"> -->
        <img :src="comment.avatar" alt="" />
      <!-- </a> -->
    </div>
    <div class="comments-item__content">
      <div class="comments-item__visitor">
        <!-- <a :href="message.link" target="_blank"> -->
          <span class="detail-visitor-name" :class="{ 'detail-visitor-name--bold': comment.nickname }">
            {{ comment.nickname }}
          </span>
        <!-- </a> -->
        <span class="detail-visitor-aite" v-if="comment.answerNickname" style="fontweight: bold">&nbsp;@&nbsp;</span>
        <span class="detail-visitor-aited" v-if="comment.answerNickname">{{ comment.answerNickname }} :</span>
      </div>

      <div class="comments-item__say">{{ comment.content }}</div>

      <div class="comments-item__detail">
        <i class="el-icon-date"></i>
        <span class="detail-visitor-date">{{ comment.createTime }}</span>
        <i
          class="el-icon-thumb"
          @click="likeComment(comment)"
          @focus="focus"
          :class="{ 'el-icon-thumb--active': comment.liked === true }"
        ></i>
        <span :class="{ 'el-icon-thumb--active': comment.liked === true }">{{ comment.likeCount }}</span>
        <i class="el-icon-chat-dot-round" @click="changeCurrentReplyMessage(comment)"></i>
        <span>{{ comment | replycCount }}</span>
      </div>
    </div>
     <login :customVisible="customVisible" @changeCustomVisible="changeCustomVisible" @getCommentByLogin="getCommentByLogin"></login>
  </div>
</template>
<script>
import { storage } from "@/utils/storage";
import login from './login';
export default {
  name: 'commentsItem',
  props: {
    subType: {
      type: Boolean,
      default: false
    },
    comment: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
     return {
      customVisible: false
    };
  },
  components: {login},
  filters: {
    replycCount(comment) {
      if (comment.children && comment.children.length) return comment.children.length
      return ''
    }
  },
  
  methods: {
    likeComment(message) {
      if (!storage.getVisitor()) {
        this.customVisible = true;
        return;
      }
      this.$emit('likeComment', message)
    },
    changeCurrentReplyMessage(message) {
      this.$emit('changeCurrentReplyMessage', message)
    },
      focus() {
      
    },
   changeCustomVisible(val) {
     this.customVisible = val
   },
    getCommentByLogin() {
     this.$emit("getComments", {})
   }
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';

.comments-item {
  display: flex;
  padding: 14px 0;
  border-bottom: 1px solid rgb(245, 240, 240);
  &__content {
    flex: 1 1 auto;
    padding-left: 20px;
  }
  &__avatar {
    width: 50px;
    height: 50px;
    flex: 0 0 auto;

    img {
      border-radius: 4px;
      width: 100%;
      height: 100%;
    }
  }
  &__avatar--small {
    width: 36px;
    height: 36px;
  }
  &__visitor {
    height: 50px;
    .detail-visitor-name {
    }
    .detail-visitor-name--bold {
      color: #eaa156;
      font-weight: 700;
    }
    .detail-visitor-aite {
    }
    .detail-visitor-aited {
    }
  }
  &__detail {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    font-size: 12px;
    // color: #909399;
    @include themeify() {
      color: themed('color-home-article-detail');
    }
    margin-top: 10px;
    i {
      font-size: 16px;
      margin-left: 12px;
      // font-weight: 600;
    }
    i:hover {
      color: #409eff;
    }
    span {
      margin-left: 6px;
    }
    .el-icon-thumb,
    .el-icon-chat-dot-round {
      cursor: pointer;
    }
    .el-icon-thumb--active {
      color: #409eff;
    }
  }
  &__say {
    word-break: break-word;
  }
}
</style>
