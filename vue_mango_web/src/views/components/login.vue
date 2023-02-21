<template>
  <div class="submit">
    <el-dialog
      title="登录"
      :visible.sync="innerCustomVisible"
      width="30%"
      custom-class="visitor-submit-box"
      @close="closeDialog"
    >
      <div class="submit__login">
        <el-form
          label-width="61px"
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
            style="display:block;margin:0 auto"
            @click="doLogin"
            :loading="submitLoading"
            >注册并登陆</el-button
          >
        </div>
        <!-- <div class="submit__third-part">
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
        </div> -->
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
import loginApi from "@/api/login";
import { storage } from "@/utils/storage";
import Vcode from "vue-puzzle-vcode";
export default {
  name: "login",
  props: {
    customVisible: {
      type: Boolean,
      default: false,
    },
  },
  components: {
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
      time: 60,
      isShow: false,
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
      submitRules: {
        nickname: [
          { required: true, validator: nameValidator, trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            required: true,
            message: "请输入邮箱",
            trigger: "blur",
          },
        ],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
      codeLoading: false,
      codeButtonName: "获取验证码",
      submitLoading: false,
      isDisabled: false,
    };
  },

  mounted() {
    this.handleQQCb();
    this.addMessageListener();
  },
  computed: {
    ...mapState(["visitorInfo"]),
    innerCustomVisible: {
      get() {
        return this.customVisible;
      },
      set(v) {},
    },
  },
  methods: {
    ...mapMutations(["setVisitor"]),
    closeDialog() {
      this.$refs["customForm"].resetFields();
      this.$emit("changeCustomVisible", false);
    },
    test() {
      this.isShow = true;
    },
    addMessageListener() {
      window.addEventListener("message", this.handleGithubCb, false);
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
              this.setVisitorInfo(res.data.data);
              this.$emit("changeCustomVisible", false);
            } else {
              // 存储
              this.tempInfo = {
                name: info.nickname,
                imgUrl: info.figureurl_qq_2 || info.figureurl_2,
                type: 1,
                qqOpenId: openId,
              };
              this.$emit("changeCustomVisible", false);
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
          this.$emit("changeCustomVisible", false);
        } else {
          const info = e.data.data;
          this.tempInfo = {
            name: info.login,
            imgUrl: info.avatar_url,
            type: 2,
            githubId: info.id,
          };
          this.$emit("changeCustomVisible", false);
          this.perfectVisible = true;
        }
      }
    },
    setVisitorInfo(info) {
      this.setVisitor(info);
      storage.setVisitor(info);
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
          }).catch((err) => {
          this.submitLoading = false;
        });

          if (res.code === 20000) {
            this.setVisitorInfo(res.data);
            this.$emit("changeCustomVisible", false);
            this.formInfo = {
              nickname: "",
              email: "",
              code: "",
            };
            this.$emit('getCommentByLogin');
          }
          this.submitLoading = false;
        }
      })
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
    openVcode() {
      this.isShow = true;
    },
  },
  destroyed() {
    if (this.timer) clearInterval(this.timer);
    window.removeEventListener("message", this.handleGithubCb);
  },
};
</script>

<style lang="scss">
@import "~@/style/index.scss";
.visitor-submit-box {
  min-width: 340px;
}
.submit {
  display: flex;
  margin-top: 24px;
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
.el-form-item__label {
  padding: 0 8px 0 0;
}
.el-input--suffix .el-input__inner {
  padding-right: 0px;
}
</style>