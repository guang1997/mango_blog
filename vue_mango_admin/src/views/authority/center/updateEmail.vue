<template>
  <div style="display: inline-block;">
    <el-dialog :visible.sync="dialog" :close-on-click-modal="false" :before-close="cancel" :title="title" append-to-body width="475px" @close="cancel">
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="88px">
        <el-form-item label="新邮箱" prop="email">
          <el-input v-model="form.email" auto-complete="on" style="width: 320px;" />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
           <div style="display: flex;width: 320px;">
              <el-input
                style="
                  vertical-align: middle;
                  box-sizing: content-box;
                  width: 100%;
                "
                v-model="form.code"
                placeholder="请输入验证码"
                clearable
              />
               <el-button
                style="
                  height: 12px;
                  vertical-align: middle;
                  box-sizing: content-box;
                "
                :disabled="isDisabled"
                size="small"
                @click="openVcode"
                :loading="codeLoading"
              >
                {{ buttonName }}
              </el-button>
           </div>
          <!-- <el-input v-model="form.code" style="width: 320px;" /> -->
        </el-form-item>
        <el-form-item label="当前密码" prop="pass">
          <el-input v-model="form.pass" type="password" style="width: 320px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取消</el-button>
        <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
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
import store from '@/store'
import { validEmail } from '@/utils/validate'
import { updateEmail } from '@/api/authority/admin'
import { sendCode } from '@/api/authority/code'
import Vcode from "vue-puzzle-vcode";
export default {
  name: "email",
  props: {
    email: {
      type: String,
      required: true
    }
  },
  components: {
    Vcode,
  },
  data() {
    const validMail = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('新邮箱不能为空'))
      } else if (value === this.email) {
        callback(new Error('新邮箱不能与旧邮箱相同'))
      } else if (validEmail(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误'))
      }
    }
    return {
      isShow: false,
      loading: false, dialog: false, title: '修改邮箱', form: { pass: '', email: '', code: '' },
      user: { email: '', password: '' }, codeLoading: false,
      buttonName: '获取验证码', isDisabled: false, time: 60,
      rules: {
        pass: [
          { required: true, message: '当前密码不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, validator: validMail, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    sendCode() {
      if (this.form.email && this.form.email !== this.email) {
        this.codeLoading = true
        this.buttonName = '验证码发送中'
        const _this = this
        sendCode(this.form).then(res => {
          this.$message({
            showClose: true,
            message: '发送成功，验证码有效期5分钟',
            type: 'success'
          })
          this.codeLoading = false
          this.isDisabled = true
          this.buttonName = this.time-- + '秒后重新发送'
          this.timer = window.setInterval(function() {
            _this.buttonName = _this.time + '秒后重新发送'
            --_this.time
            if (_this.time < 0) {
              _this.buttonName = '重新发送'
              _this.time = 60
              _this.isDisabled = false
              window.clearInterval(_this.timer)
            }
          }, 1000)
        }).catch(err => {
          this.resetForm()
          this.codeLoading = false
          console.log(err)
        })
      }
    },
    doSubmit() {
      
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.form.oldEmail = this.email
          console.log(this.form)
          updateEmail(this.form).then(res => {
            this.loading = false
            this.resetForm()
            this.$notify({
              title: '邮箱修改成功',
              type: 'success',
              duration: 1500
            })
            store.dispatch('GetInfo').then((response) => {
              this.$emit('changeEmail', response.data.email);
            })
          }).catch(err => {
            this.loading = false
            console.log(err.response.data.message)
          })
        } else {
          return false
        }
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      window.clearInterval(this.timer)
      this.time = 60
      this.buttonName = '获取验证码'
      this.isDisabled = false
      this.form = { pass: '', email: '', code: '' }
    },
    // 用户通过了验证
    success(msg) {
      this.isShow = false; // 通过验证后，需要手动隐藏模态框
      this.sendCode();
    },
    // 用户点击遮罩层，应该关闭模态框
    close() {
      this.isShow = false;
    },
    openVcode() {
      this.isShow = true;
    },
  }
}
</script>

<style scoped>

</style>
