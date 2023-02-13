<template>
  <div class="message-board">
    <layout _title="友情链接" cover="/static/img/cover/msgboard.jpeg">
      <div class="statement">
        <p>
          首先将需要接入本博客站点，然后给我<a
            href="javascript:void(0)"
            @click="showDialog()"
            >留言</a
          >提供您站点的如下信息：
        </p>
        <quote>
          <p>站点名称：{{ websiteInfo.name }}</p>
          <p>站点链接：{{ websiteInfo.domain }}</p>
          <p>简短描述：{{ websiteInfo.desc }}</p>
        </quote>
        <p>接入成功后将会以邮件的方式通知。</p>
      </div>
      <!----->
      <hr />
      <div class="friend-list animate">
        <div class="friend-item" v-for="item in friendLinkList" :key="item.id">
          <a target="_blank" :href="item.url"
            ><div class="site-name">{{ item.title }}</div>
            <div class="site-detail">{{ item.summary }}</div></a
          >
        </div>
      </div>

      <el-dialog
        title="提交友链"
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
            <el-form-item label="站点名称" prop="title" label-width="80px">
              <el-input
                style="display: flex"
                v-model="formInfo.title"
                placeholder="请输入站点名称"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item class="live2d_link_url" label="站点链接" prop="url" label-width="80px">
              <el-input
                style="display: flex"
                v-model="formInfo.url"
                placeholder="请输入站点链接"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="简短描述" prop="summary" label-width="80px">
              <el-input
                style="display: flex"
                v-model="formInfo.summary"
                placeholder="请输入简短描述"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email" label-width="80px">
              <el-input
                style="display: flex"
                v-model="formInfo.email"
                placeholder="请输入邮箱"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="code" label-width="80px">
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
              @click="saveFriendLink"
              :loading="submitLoading"
              >提交</el-button
            >
          </div>
        </div>
      </el-dialog>
      <Vcode
        :show="isShow"
        @success="success"
        @close="close"
        style="z-index: 10000"
      />
    </layout>
  </div>
</template>
<script>
import { mapState } from "vuex";
import quote from "@/components/quote";
import linkApi from "@/api/link";
import loginApi from "@/api/login";
import Vcode from "vue-puzzle-vcode";
export default {
  name: "friendLink",
  metaInfo() {
    return {
      title: `友情链接  - Lisite's Blog`,
    };
  },
  components: {
    quote,
    Vcode,
  },
  data() {
    const urlValidator = (rule, value, callback) => {
      const reg =
        /^((http|https):\/\/)?(([A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\.)+([A-Za-z]+)[/\?\:]?.*$/;
      if (value === "") {
        callback(new Error("请输入站点链接"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入正确的站点链接"));
      } else {
        callback();
      }
    };
    return {
      currentPage: 1,
      total: 0,
      pageSize: 10,
      messages: [],
      websiteInfo: {
        name: "李斯特",
        domain: "http://localhost:8080",
        desc: "测试",
      },
      friendLinkList: [],
      customVisible: false,
      time: 60,
      isShow: false,
      formInfo: {
        title: "",
        email: "",
        code: "",
        url: "",
        summary: "",
      },
      codeLoading: false,
      codeButtonName: "获取验证码",
      submitLoading: false,
      isDisabled: false,
      submitRules: {
        title: [{ required: true, trigger: "blur", message: "请输入站点名称" }],
        url: [
          {
            required: true,
            validator: urlValidator,
            trigger: "blur",
            message: "请输入站点链接",
          },
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
    };
  },
  created() {
    this.getFriendLink();
  },
  computed: {
    ...mapState(["visitorInfo"]),
  },
  methods: {
    saveFriendLink() {
      this.$refs.customForm.validate(async (valid) => {
        this.submitLoading = true;
        if (valid) {
          const res = await linkApi
            .saveFriendLink({
              email: this.formInfo.email,
              title: this.formInfo.title,
              code: this.formInfo.code,
              summary: this.formInfo.summary,
              url: this.formInfo.url,
            })
            .catch((err) => {
              this.submitLoading = false;
            });

          if (res.code === 20000) {
            this.formInfo = {
              title: "",
              email: "",
              code: "",
              url: "",
              summary: "",
            };
            this.$message({
              showClose: true,
              message: "保存成功，请等待管理员审核",
              type: "success",
            });
          } else {
            this.$message({
              showClose: true,
              message: res.message,
              type: "error",
            });
          }
          this.submitLoading = false;
          this.customVisible = false;
        }
      });
    },
    closeDialog() {
      this.$refs["customForm"].resetFields();
    },
    sendCode() {
      if (this.formInfo.email) {
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
      }
    },
    showDialog() {
      this.customVisible = true;
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
    getFriendLink() {
      linkApi.getFriendLink({}).then((response) => {
        if (response.code == 20000) {
          this.friendLinkList = response.data;
        }
      });
    },
  },
};
</script>
<style lang="scss">
@import "~@/style/index.scss";
.statement {
  a {
    color: #ff6d6d;
  }
  p {
    line-height: 2rem;
    font-size: 20px;
    color: #6f6f6f;
  }
}
hr {
  margin: 40px 0;
  border: 0;
  height: 1px;
  background-image: linear-gradient(
    to right,
    rgba(0, 0, 0, 0),
    rgba(255, 109, 109, 0.75),
    rgba(0, 0, 0, 0)
  );
}
.friend-list {
  width: 100%;
  .friend-item {
    display: inline-block;
    width: 30%;
    /*height: 100px;*/
    margin: 0 5% 20px 0;
    padding: 10px 30px;
    border: 2px solid #ececec;
    border-radius: 3px;
    &:hover {
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
    &:nth-of-type(3n) {
      margin-right: 0;
    }
    .site-name,
    .site-detail {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      padding-bottom: 10px;
      line-height: 1.5rem;
      font-size: 25px;
    }
    .site-name {
      color: #8fd0cc;
      border-bottom: 1px dotted #ececec;
    }
    .site-detail {
      font-size: 13px;
      padding-top: 10px;
    }
  }
}
/*******/
@media (max-width: 800px) {
  .friend-header {
    margin-top: 0;
  }
  .friend-list {
    .friend-item {
      width: 45%;
      &:nth-of-type(2n) {
        margin-right: 0;
      }
      &:nth-of-type(3n) {
        margin-right: 5%;
      }
    }
  }
}
@media (max-width: 600px) {
  .friend-list {
    .friend-item {
      display: block;
      width: 90%;
      margin: 0 auto 20px auto;
      &:nth-of-type(2n) {
        margin-right: auto;
      }
      &:nth-of-type(3n) {
        margin-right: auto;
      }
    }
  }
}
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
