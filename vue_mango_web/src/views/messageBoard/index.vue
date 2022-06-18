<template>
  <div class="message-board">
    <layout _title="留言板" cover="/static/img/cover/msgboard.jpeg">
      <div class="message-board__content">
        <note>
          <p class="message-board__welcome">
            you must be a surprise someone is looking forward to
          </p>
        </note>
        <splitLine></splitLine>
        <div class="message-board__title">
          <i class="el-icon-chat-dot-round"></i>
          <span>留言</span>
        </div>

        <div class="message-board__submit">
          <submit @submitContent="submitContent"></submit>
        </div>

        <div class="message-board__list">
          <div class="message-board__total">
            <span>{{ total }}条留言</span>
          </div>
          <div class="message-board__list">
            <comments
              :comments="messages"
              @submitReply="submitReply"
              @likeComment="likeComment"
            ></comments>
            <div class="message-board__page" v-if="messages.length">
              <el-pagination
                :current-page.sync="currentPage"
                :total="total"
                layout="prev, pager, next"
                :page-size="pageSize"
                @current-change="currentChange"
              ></el-pagination>
            </div>
          </div>
        </div>
      </div>
    </layout>
  </div>
</template>
<script>
import { mapState, mapMutations } from "vuex";
import note from "@/components/note/";
import splitLine from "@/components/splitLine/";
import submit from "@/views/components/submit";
import comments from "@/views/components/comments";
import commentApi from "@/api/comment";
import { storage } from "@/utils/storage";
import loginApi from "@/api/login";
export default {
  name: "messageBoard",
  metaInfo() {
    return {
      title: `留言板  - Lisite's Blog`,
    };
  },
  components: {
    note,
    submit,
    splitLine,
    comments,
  },
  data() {
    return {
      currentPage: 1,
      total: 0,
      pageSize: 10,
      messages: [],
    };
  },
  async asyncData() {
    const msgRes = await commentApi.getMessageBoardCommentByPage({
      page: 1,
      size: 10,
      source: "MESSAGE_BOARD_MESSAGE",
      queryLike: true,
      userId: storage.getVisitor() ? storage.getVisitor().id : "",
    });
    if (msgRes.code === 20000)
      return { messages: msgRes.data.data, total: msgRes.data.total };
  },
  computed: {
    ...mapState(["visitorInfo"]),
  },
  created() {
    this.updateVisitorInfo()
  },
  methods: {
     ...mapMutations(["setVisitor"]),
    submitContent(content, cb) {
      this.submit(content, null, cb);
    },
    submitReply(content, currentReplyMessage, cb) {
      this.submit(content, currentReplyMessage, cb);
    },
    async submit(content, currentReplyMessage, cb) {
      if(this.visitorInfo.commentStatus == 0) {
        this.$message({
          type: "warning",
          message: "您被禁言了，请联系管理员处理",
        });
        return;
      }
      let parentId = "0";
      let answerNickname;
      if (currentReplyMessage) {
        if (currentReplyMessage.parentId === "0")
          parentId = currentReplyMessage.id;
        else parentId = currentReplyMessage.parentId;
        answerNickname = currentReplyMessage.nickname;
      }
      const res = await commentApi.saveComment({
        nickname: this.visitorInfo.nickname,
        avatar: this.visitorInfo.avatar,
        userId: this.visitorInfo.id,
        content: content,
        parentId,
        source: "MESSAGE_BOARD_MESSAGE",
        answerNickname,
      });
      if (res.code === 20000) {
        if (cb) cb();
        this.$message({
          type: "success",
          message: "留言成功",
        });
        this.getMessageBoardCommentByPage();
      }
    },
    async getMessageBoardCommentByPage() {
      const msgRes = await commentApi.getMessageBoardCommentByPage({
        page: this.currentPage,
        size: this.pageSize,
        source: "MESSAGE_BOARD_MESSAGE",
        queryLike: true,
        userId: this.visitorInfo.id,
      });
      if (msgRes.code === 20000) {
        this.messages = msgRes.data.data;
        this.total = msgRes.data.total;
      }
    },
    async likeComment(message) {
      const isLiked = message.liked ? true : false;
      const likeRes = await commentApi.likeComment({
        isLiked,
        userId: this.visitorInfo.id,
        source: "MESSAGE_BOARD_LIKES",
        parentId: message.id,
        avatar:
          this.visitorInfo && this.visitorInfo.avatar
            ? this.visitorInfo.avatar
            : "",
        nickname:
          this.visitorInfo && this.visitorInfo.nickname
            ? this.visitorInfo.nickname
            : "",
      });
      if (likeRes.code === 20000) {
        this.$message({
          type: "success",
          message: likeRes.message,
        });
        this.getMessageBoardCommentByPage();
      }
    },
   updateVisitorInfo() {
    // 如果用户没有退出账号就关闭网页了，再次打开网页的时候去更新网页
    if(storage.getVisitor()) {
      loginApi.getUser(storage.getVisitor()).then(res => {
        if (res.code === 20000) {
            this.setVisitorInfo(res.data.data);
        }
      })
    }
  },
  setVisitorInfo(info) {
      this.setVisitor(info);
      storage.setVisitor(info);
    },
    currentChange() {
      this.getMessageBoardCommentByPage();
    },
  },
};
</script>
<style lang="scss">
@import "~@/style/index.scss";

.message-board {
  &__welcome {
    font-family: "sf-arch";
    font-size: 28px;
    line-height: 1.8;
    @include respond-to(xs) {
      font-size: 18px;
    }
  }
  &__title {
    padding: 16px 0;
    font-size: 20px;
    font-weight: 700;
    > [class^="el-icon-"] {
      font-weight: 700;
    }
    span {
      margin-left: 12px;
    }
  }
  &__list {
    margin-top: 28px;
  }
  &__total {
    color: #4c4948;
    font-size: 25px;
    font-weight: bold;
  }
  &__page {
    @include flex-box-center;
    padding: 16px 0;
  }
}
</style>
