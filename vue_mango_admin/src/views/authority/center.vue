<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="5" style="margin-bottom: 10px">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div style="text-align: center">
              <div class="el-upload">
                <img :src="userAvatar ? userAvatar : Avatar" title="点击上传头像" class="avatar" @click="toggleShow">
                <myUpload
                  :modelValue.sync="show"
                  field="avatar"
                  :headers="headers"
                  :url="uploadAvatarApi"
                  :params="{moduleName:'avatar', id: user.id}"
                  @crop-upload-success="cropUploadSuccess"
                />
              </div>
            </div>
            <ul class="user-info">
              <li><div style="height: 100%"><svg-icon icon-class="login" /> 登录账号<div class="user-right">{{ user.username }}</div></div></li>
              <li><svg-icon icon-class="user1" /> 用户昵称 <div class="user-right">{{ user.nickname }}</div></li>
              <li><svg-icon icon-class="phone" /> 手机号码 <div class="user-right">{{ user.phone }}</div></li>
              <li><svg-icon icon-class="email" /> 用户邮箱 <div class="user-right">{{ user.email }}</div></li>
              <li>
                <svg-icon icon-class="anq" /> 安全设置
                <div class="user-right">
                  <a @click="$refs.password.dialog = true">修改密码 </a>
                  <a @click="$refs.email.dialog = true">修改邮箱</a>
                </div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="16" :lg="18" :xl="19">
        <!--    用户资料    -->
        <el-card class="box-card">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="用户资料" name="first">
              <el-form ref="form" :model="form" :rules="rules" style="margin-top: 10px;" size="small" label-width="65px">
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="form.nickname" style="width: 35%" />
                  <span style="color: #C0C0C0;margin-left: 10px;">用户昵称不作为登录使用</span>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="form.phone" style="width: 35%;" />
                  <span style="color: #C0C0C0;margin-left: 10px;">手机号码不能重复</span>
                </el-form-item>
                  <el-form-item label="qq号" prop="qqNumber">
                  <el-input v-model="form.qqNumber" style="width: 35%;" />
                  <span style="color: #C0C0C0;margin-left: 10px;">qq号不能重复</span>
                </el-form-item>
                  <el-form-item label="微信号" prop="weChat">
                  <el-input v-model="form.weChat" style="width: 35%;" />
                  <span style="color: #C0C0C0;margin-left: 10px;">微信号不能重复</span>
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="form.gender" style="width: 178px">
                    <el-radio label="1">男</el-radio>
                    <el-radio label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="">
                  <el-button :loading="saveLoading" size="mini" type="primary" @click="doSubmit">保存配置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <updatePass ref="password" />
    <updateEmail v-if="flag" ref="email" :email="user.email" @changeEmail="changeEmailByChild"/>
  </div>
</template>

<script>
import myUpload from 'vue-image-crop-upload'
import { mapGetters } from 'vuex'
import  updatePass from '@/views/authority/center/updatePass'
import  updateEmail from '@/views/authority/center/updateEmail'
import { getToken } from '@/utils/auth'
import store from '@/store'
import crud from '@/mixins/crud'
import { isvalidPhone } from '@/utils/validate'
import { editAdminFromCenter } from '@/api/authority/admin'
import Avatar from '@/assets/images/avatar.png'
export default {
  name: 'Center',
  components: { updatePass, myUpload, updateEmail },
  mixins: [crud],
  data() {
    // 自定义验证
    const validPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入电话号码'))
      } else if (!isvalidPhone(value)) {
        callback(new Error('请输入正确的11位手机号码'))
      } else {
        callback()
      }
    }
    return {
      show: false,
      Avatar: Avatar,
      userAvatar: null,
      activeName: 'first',
      saveLoading: false,
      user: {},
      headers: {
        'Authorization': getToken()
      },
      flag: false,
      form: {},
      rules: {
        nickname: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
         phone: [
          { required: true, trigger: 'blur', validator: validPhone }
        ],
        qqNumber: [
          { required: true, trigger: 'blur' }
        ],
         weChat: [
          { required: true, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'uploadAvatarApi',
      'baseApi'
    ])
  },
  created() {
    store.dispatch('GetInfo').then(response => {
      let user = response.data
      this.user = {id: user.id, username: user.username, nickname: user.nickname, phone: user.phone, email: user.email, avatar: user.avatar}
      this.form = { id: user.id, nickname: user.nickname, gender: user.gender, phone: user.phone ,qqNumber: user.qqNumber, weChat: user.weChat}
      if(this.user.email) {
        this.flag = true
      }
      this.userAvatar = user.avatar
    })
    
    },
  methods: {
    toggleShow() {
      this.show = !this.show
    },
    handleClick(tab, event) {
      if (tab.name === 'second') {
        this.init()
      }
    },
    cropUploadSuccess(jsonData, field) {
      store.dispatch('GetInfo').then(res => {
        this.userAvatar = res.data.avatar
      })
    },
    doSubmit() {
      if (this.$refs['form']) {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.saveLoading = true
            editAdminFromCenter(this.form).then(() => {
              this.editSuccessNotify()
              store.dispatch('GetInfo').then(() => {
                })
              this.saveLoading = false
            }).catch(() => {
              this.saveLoading = false
            })
          }
        })
      }
    },
    changeEmailByChild(email) {
        this.user.email = email
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .avatar {
    width: 120px;
    height: 120px;
    border-radius: 50%;
  }
  .user-info {
    padding-left: 0;
    list-style: none;
    li{
      border-bottom: 1px solid #F0F3F4;
      padding: 11px 0;
      font-size: 13px;
    }
    .user-right {
      float: right;
      a{
        color: #317EF3;
      }
    }
  }
</style>
