<template>
  <div class="comments">
    <div class="comments__top" v-for="(msg, index) in comments" :key="index">
      <comments-item
        :comment="msg"
        @changeCurrentReplyMessage="changeCurrentReplyMessage"
        @likeComment="likeComment"
         @getComments="getComments"
      ></comments-item>
      <submit
        v-if="msg.id === currentReplyMessage.id"
        :currentReplyMessage="currentReplyMessage"
        @changeCurrentReplyMessage="changeCurrentReplyMessage"
        @submitContent="submitContent"
        @getComments="getComments"
      ></submit>
      <div class="comments__sub" v-if="msg.children && msg.children.length">
        <template v-for="(reply, _index) in msg.children">
          <comments-item
            :key="'reply_' + _index"
            :comment="reply"
            :subType="true"
            @likeComment="likeComment"
            @changeCurrentReplyMessage="changeCurrentReplyMessage"
             @getComments="getComments"
          ></comments-item>
          <submit
            v-if="reply.id === currentReplyMessage.id"
            :key="_index"
            :currentReplyMessage="currentReplyMessage"
            @submitContent="submitContent"
            @changeCurrentReplyMessage="changeCurrentReplyMessage"
            @getComments="getComments"
          ></submit>
        </template>
      </div>
    </div>
    <div class="comments__empty" v-if="!comments.length">emm...没人理我</div>
  </div>
</template>
<script>
import commentsItem from './comments-item.vue'
import submit from './submit'
export default {
  name: 'comments',
  props: {
    comments: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      currentReplyMessage: {}
    }
  },
  components: {
    commentsItem,
    submit
  },
  methods: {
    submitContent(content, cb) {
      this.$emit('submitReply', content, this.currentReplyMessage, cb)
    },
    changeCurrentReplyMessage(message) {
      this.currentReplyMessage = message
    },
    likeComment(message) {
      this.$emit('likeComment', message)
    },
    getComments() {
      this.$emit('getCommentByPage');
    }
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';

.comments {
  @include themeify() {
    color: themed('color-comments');
  }
  &__top {
  }
  &__sub {
    padding-left: 50px;
    @include respond-to(xs) {
      padding-left: 20px;
    }
  }

  &__empty {
    padding: 16px;
    text-align: center;
    @include themeify() {
      color: themed('color-ele-holder');
    }
  }
}
</style>
