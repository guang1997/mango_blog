<template>
  <div class="submit">
    <div class="submit__avatar">
      <div class="submit__avatar-default">
        <img
          v-show="!!visitorInfo.avatar"
          :src="visitorInfo.avatar"
          :title="visitorInfo.nickname"
        />
        <i
          v-show="!visitorInfo.avatar"
          class="el-icon-user"
          :title="visitorInfo.nickname"
        ></i>
      </div>
      <div class="submit__avatar-rel"></div>
    </div>
    <div class="submit__content">
      <div class="submit__input">
        <span v-if="currentReplyMessage.nickname" class="reply-name"
          >回复 {{ currentReplyMessage.nickname }} :</span
        >
        <el-input
          ref="comment"
          type="textarea"
          :rows="3"
          placeholder="说点什么"
          @focus="focus"
          v-model="comment"
          maxlength="200"
          show-word-limit
        ></el-input>
      </div>
      <div class="submit__handle">
        <div class="submit__emoji-userTag">
          <div class="submit__emoji">
            <emoji @getEmoji="getEmoji"></emoji>
          </div>
          <div class="submit__userTag" v-show="visitorInfo.nickname">
            <span>欢迎，{{ visitorInfo.nickname }}</span>
            <i class="el-icon-circle-close" title="退出" @click="logout"></i>
          </div>
        </div>

        <div class="submit__btn">
          <el-button
            v-if="currentReplyMessage.id"
            size="medium"
            @click="cancelReply"
            >取消</el-button
          >
          <el-button
            size="medium"
            :disabled="!visitorInfo.id"
            @click="submitMessage"
            >提交</el-button
          >
        </div>
      </div>
    </div>
    <login :customVisible="customVisible" @changeCustomVisible="changeCustomVisible" @getCommentByLogin="getCommentByLogin"></login>
    
  </div>
</template>
<script>
import { mapState, mapMutations } from "vuex";
import login from './login';
import emoji from "@/components/emoji";
import { storage } from "@/utils/storage";
export default {
  name: "submit",
  props: {
    currentReplyMessage: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  components: {
    emoji,
    login,
  },
  data() {
   
    return {
      comment: "",
      customVisible: false
    };
  },
  mounted() {

  },
  computed: {
    ...mapState(["visitorInfo"]),
  },
  methods: {
    ...mapMutations(["setVisitor"]),
   
    submitMessage() {
      if (this.comment.trim() === "") {
        this.$message({
          type: "warning",
          message: "Oops 至少得说两句~",
        });
        return;
      }
      this.$emit("submitContent", this.comment.trim(), () => {
        this.comment = "";
      });
    },
    cancelReply() {
      this.$emit("changeCurrentReplyMessage", {});
    },
   
    getEmoji(emoji) {
      this.$refs.comment.focus();
      this.comment += emoji;
    },
      focus() {
      if (!storage.getVisitor()) this.customVisible = true;
    },
    logout() {
      this.$confirm(`确定退出账号吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.setVisitor({});
          storage.removeVisitor();
          this.$emit("getComments", {})
        })
        .catch(() => {});
    },
   changeCustomVisible(val) {
     this.customVisible = val
   },
   getCommentByLogin() {
     this.$emit("getComments", {})
   }
  },
  destroyed() {
   
  },
};
</script>
<style lang="scss">
@import "~@/style/index.scss";

.submit {
  display: flex;
  margin-top: 24px;
  &__avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    @include themeify() {
      color: themed("color-avatar-icon");
      background: themed("color-avatar-bg");
    }
    &-default {
      @include flex-box-center;
      width: 100%;
      height: 100%;
      .el-icon-user {
        font-size: 42px;
      }
      img {
        width: 100%;
        height: 100%;
        border-radius: 50% 50%;
      }
    }
  }
  &__content {
    display: flex;
    flex-direction: column;
    flex: 1 1 auto;
    margin-left: 18px;
  }
  &__handle {
    display: flex;
    justify-content: space-between;
    padding: 12px 0;
  }
  &__emoji-userTag {
    display: flex;
    align-items: flex-start;
  }
  &__userTag {
    margin-left: 28px;
    color: #909399;
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all ease 0.38s;
    .el-icon-circle-close {
      margin-left: 8px;
      font-size: 16px;
      opacity: 0;
      transition: all ease 0.38s;
    }
  }

  &__userTag:hover {
    color: #409eff;
    .el-icon-circle-close {
      opacity: 1;
    }
  }
  &__input {
    width: 100%;
    .reply-name {
      display: inline-block;
      margin-bottom: 4px;
    }
  }

  
  &__perfect {
    padding: 0 20px 0 0;
  }
  &__perfect-footer {
    text-align: right;
  }
}
.el-form-item__label {
  padding: 0 8px 0 0;
}
.el-input--suffix .el-input__inner {
  padding-right: 0px;
}
</style>
