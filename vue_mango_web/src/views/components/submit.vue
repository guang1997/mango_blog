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
    <el-dialog
      title="登录"
      :visible.sync="customVisible"
      width="30%"
      custom-class="visitor-submit-box"
      @close="closeDialog"
    >
      <div class="submit__login">
        <el-form
          label-width="60px"
          :model="formInfo"
          :rules="submitRules"
          ref="customForm"
        >
          <el-form-item label="昵称" prop="nickname">
            <el-input
              v-model="formInfo.nickname"
              placeholder="请输入昵称"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="formInfo.email"
              placeholder="请输入邮箱"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="code">
            <div style="display: flex">
              <el-input
                style="
                  vertical-align: middle;
                  box-sizing: content-box;
                  width: 100%;
                "
                v-model="formInfo.code"
                placeholder="请输入验证码"
                clearable
              />
              <el-button
                style="
                  height: 20px;
                  vertical-align: middle;
                  box-sizing: content-box;
                "
                :disabled="isDisabled"
                size="small"
                @click="openVcode"
                :loading="codeLoading"
              >
                {{ codeButtonName }}
              </el-button>
            </div>
          </el-form-item>
        </el-form>
        <div class="submit__register">
          <el-button
            size="small"
            type="primary"
            @click="doLogin"
            :loading="submitLoading"
            >登陆</el-button
          >
        </div>
        <div class="submit__third-part">
          <div class="line">第三方登录</div>
          <div class="submit__third-app">
            <a href="javascript: void(0)" @click="openQQ" class="login-qq">
              <img src="~@/assets/img/qq.png" alt="QQ登录" />
            </a>
            <a
              href="javascript: void(0)"
              class="login-github"
              @click="openGithub"
            >
              <img src="~@/assets/img/github.png" alt="github登录" />
            </a>
          </div>
        </div>
      </div>
    </el-dialog>
    <Vcode
      :show="isShow"
      @success="success"
      @close="close"
      style="z-index: 10000"
    />
  </div>
</template>
<script>
import { mapState, mapMutations } from "vuex";
import { storage } from "@/utils/storage";
import emoji from "@/components/emoji";
import loginApi from "@/api/login";
import Vcode from "vue-puzzle-vcode";

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
    Vcode,
  },
  data() {
    const nameValidator = (rule, value, callback) => {
      const reg = /^[\u4e00-\u9fa5_a-zA-Z0-9]{1,16}$/gi;
      if (value === "") {
        callback(new Error("请输入昵称"));
      } else if (!reg.test(value)) {
        callback(new Error("昵称支持中英文、数字、下划线的组合，限16位"));
      } else {
        callback();
      }
    };
    return {
      comment: "",
      customVisible: false,
      perfectVisible: false,
      formInfo: {
        nickname: "",
        email: "",
        code: "",
      },
      perfect: {
        email: "",
        link: "",
      },
      tempInfo: {},
      submitRules: {
        nickname: [
          { required: true, validator: nameValidator, trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            required: true,
            message: "请填写邮箱",
            trigger: "blur",
          },
        ],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
      isDisabled: false,
      time: 60,
      isShow: false,
      codeLoading: false,
      codeButtonName: "获取验证码",
      submitLoading: false,
    };
  },
  mounted() {
    this.handleQQCb();
    this.addMessageListener();
  },
  computed: {
    ...mapState(["visitorInfo"]),
  },
  methods: {
    ...mapMutations(["setVisitor"]),
    test() {
      this.isShow = true;
    },
    // 用户通过了验证
    success(msg) {
      this.isShow = false; // 通过验证后，需要手动隐藏模态框
      if (this.formInfo.email) {
        this.buttonName = "验证码发送中";
        this.codeLoading = true;
        this.sendCode();
      } else {
        this.$message({
          type: "warning",
          message: "请填写正确的邮箱~",
        });
      }
    },
    // 用户点击遮罩层，应该关闭模态框
    close() {
      this.isShow = false;
    },
    openGithub() {
      // TODO: 环境变量 BASE_URL
      const VUE_APP_BASE_API = process.env.VUE_APP_BASE_API;
      window.open(
        `${VUE_APP_BASE_API}/login/git`,
        "_blank",
        "height=600,width=800,toolbar=no, menubar=no, scrollbars=no"
      );
    },
    openQQ() {
      // return
      // this.qq_win = window.open(
      //   'https://graph.qq.com/oauth2.0/authorize?client_id=&response_type=token&scope=all&redirect_uri=https://www.mapblog.cn/qc_back.html',
      //   'oauth2Login_10000',
      //   'height=525,width=585, toolbar=no, menubar=no, scrollbars=no, status=no, location=yes, resizable=yes'
      // )
      QC.Login.showPopup({
        appId: "101999089",
        redirectURI: "http://mapblog.cn/qc_back.html",
      });
    },
    doLogin() {
      this.$refs.customForm.validate(async (valid) => {
        this.submitLoading = true;
        if (valid) {
          const res = await loginApi.doLogin({
            email: this.formInfo.email,
            nickname: this.formInfo.nickname,
            code: this.formInfo.code,
            avatar:
              "https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg",
          });

          if (res.code === 20000) {
            this.setVisitorInfo(res.data.data);
            this.customVisible = false;
            this.formInfo = {
              nickname: "",
              email: "",
              code: "",
            };
          }
          this.submitLoading = false;
        }
      });
    },
    addMessageListener() {
      window.addEventListener("message", this.handleGithubCb, false);
    },
    handleQQCb() {
      QC.Login({}, (info) => {
        // 获取opeId accessToken
        QC.Login.getMe(async (openId) => {
          // 查看QQ用户是否被存储了
          const res = await this.$api.isExistedVisitor({
            name: info.nickname,
            imgUrl: info.figureurl_qq_2 || info.figureurl_2,
            qqOpenId: openId,
            type: 1,
          });

          if (res.status === 200) {
            if (res.data._saved) {
              this.setVisitorInfo(res.data.info);
              this.customVisible = false;
            } else {
              // 存储
              this.tempInfo = {
                name: info.nickname,
                imgUrl: info.figureurl_qq_2 || info.figureurl_2,
                type: 1,
                qqOpenId: openId,
              };
              this.customVisible = false;
              this.perfectVisible = true;
            }
          }
        });
      });
    },
    handleGithubCb(e) {
      if (e.data.type === "github") {
        console.log(
          "github登陆成功=====>>>>",
          e.data.data,
          "isSaved::::",
          e.data.data._saved
        );
        // 此用户已登陆过
        if (e.data.data._saved) {
          delete e.data.data._saved;
          // 初始化访客信息
          this.setVisitorInfo(e.data.data);
          this.customVisible = false;
        } else {
          const info = e.data.data;
          this.tempInfo = {
            name: info.login,
            imgUrl: info.avatar_url,
            type: 2,
            githubId: info.id,
          };
          this.customVisible = false;
          this.perfectVisible = true;
        }
      }
    },
    setVisitorInfo(info) {
      this.setVisitor(info);
      storage.setVisitor(info);
    },
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
    logout() {
      this.$confirm(`确定退出账号吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.setVisitor({});
          storage.removeVisitor();
        })
        .catch(() => {});
    },
    focus() {
      if (!storage.getVisitor()) this.customVisible = true;
    },
    getEmoji(emoji) {
      this.$refs.comment.focus();
      this.comment += emoji;
    },
    openVcode() {
      this.isShow = true;
    },
    sendCode() {
      // if (this.formInfo.email) {
      this.buttonName = "验证码发送中";
      const _this = this;
      const param = {
        email: this.formInfo.email,
      };
      loginApi
        .sendCode(param)
        .then((res) => {
          this.isDisabled = true;
          this.$message({
            showClose: true,
            message: "发送成功，验证码有效期5分钟",
            type: "success",
          });
          this.codeLoading = false;
          this.codeButtonName = "获取验证码";
          this.codeButtonName = this.time-- + "秒后重新发送";
          this.timer = window.setInterval(function () {
            _this.codeButtonName = _this.time + "秒后重新发送";
            --_this.time;
            if (_this.time < 0) {
              _this.codeButtonName = "重新发送";
              _this.time = 60;
              _this.isDisabled = false;
              window.clearInterval(_this.timer);
            }
          }, 1000);
        })
        .catch((err) => {
          this.isDisabled = false;
          this.codeLoading = false;
          console.log(err);
        });
      // }
    },
    closeDialog() {
      this.$refs["customForm"].resetFields();
    },
  },
  destroyed() {
    window.removeEventListener("message", this.handleGithubCb);
    if (this.timer) clearInterval(this.timer);
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
  &__login {
    padding: 0 20px 0 0;
  }
  &__register {
    text-align: right;
  }
  &__third-part {
    .line {
      color: #b9b9b9;
      margin: 15px 0;
      font-size: 10px;
      text-align: center;
    }
  }
  &__third-app {
    display: flex;
    justify-content: center;
    a {
      display: inline-block;
      width: 50px;
      height: 50px;
      margin: 20px;
    }
    img {
      border: none;
      width: 100%;
      height: 100%;
    }
  }
  &__perfect {
    padding: 0 20px 0 0;
  }
  &__perfect-footer {
    text-align: right;
  }
}
.visitor-submit-box {
  min-width: 340px;
}
.codeSpan {
  color: #67c23a;
  font-size: 12px;
  line-height: 1;
  padding-top: 4px;
  position: absolute;
  top: 100%;
  left: 0;
}
.el-form-item__label {
  padding: 0 8px 0 0;
}
.el-input--suffix .el-input__inner {
  padding-right: 0px;
}
</style>
