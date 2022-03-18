<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.blurry"
          size="small"
          clearable
          placeholder="输入用户名或者昵称搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select v-model="query.gender" clearable size="small" placeholder="性别" class="filter-item" style="width: 90px" @change="crud.toQuery">
          <el-option v-for="item in genderTypeOptions" :key="item.key" :label="item.value" :value="item.key" />
        </el-select>
        <el-select v-model="query.enabled" clearable size="small" placeholder="状态" class="filter-item" style="width: 90px" @change="crud.toQuery">
          <el-option v-for="item in enabledTypeOptions" :key="item.key" :label="item.value" :value="item.key" />
        </el-select>
        <date-range-picker v-model="query.lastLoginTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
          <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="570px">
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="66px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" @keydown.native="keydown($event)" />
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model.number="form.mobile" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="form.nickname" @keydown.native="keydown($event)" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" />
            </el-form-item>
             <el-form-item label="qq" prop="qqNumber">
              <el-input v-model="form.qqNumber" />
            </el-form-item>
             <el-form-item label="微信" prop="weChat">
              <el-input v-model="form.weChat" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender" style="width: 178px">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="状态">
              <el-radio-group v-model="form.enabled" >
                <el-radio
                  v-for="item in enabledTypeOptions"
                  :key="item.key"
                  :label="item.value"
                >{{ item.value }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item style="margin-bottom: 0;" label="角色" prop="roles">
              <el-select
                v-model="roleDatas"
                style="width: 437px"
                multiple
                placeholder="请选择"
                @remove-tag="deleteTag"
                @change="changeRole"
              >
                <el-option
                  v-for="item in roles"
                  :key="item.id"
                  :label="item.roleName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
             <el-form-item label="头像" prop="avatar">
              <el-upload
                class="avatar-uploader"
                :action="uploadURL()"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :auto-upload="false"
                :before-upload="beforeAvatarUpload">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button
          :loading="crud.status.cu === 2"
          type="primary"
          @click="crud.submitCU"
          >确认</el-button
        >
      </div>
    </el-dialog>
    <el-row :gutter="15">
      <!--管理员管理-->
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">管理员列表</span>
          </div>
          <el-table
            ref="table"
            v-loading="crud.loading"
            highlight-current-row
            style="width: 100%"
            :data="crud.data"
            @selection-change="crud.selectionChangeHandler"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column label="头像" width="120" align="center">
              <template slot-scope="scope">
                <img
                  :src="scope.row.avatar"
                  style="width: 100px;height: 100px;"
                >
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" />
            <el-table-column :show-overflow-tooltip="true" prop="nickname" label="昵称" />
            <el-table-column label="状态" align="center" prop="enabled">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.enabled"
                active-color="#409EFF"
                inactive-color="#F56C6C"
                @change="changeEnabled(scope.row, scope.row.enabled)"
              />
            </template>
          </el-table-column>
            <el-table-column label="性别" width="100" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.gender==='1'" type="success">男</el-tag>
                <el-tag v-if="scope.row.gender==='2'" type="danger">女</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="登录次数" width="100" prop="loginCout">
              <template slot-scope="scope">
                <span>{{ scope.row.loginCount }}</span>
              </template>
            </el-table-column>
            <el-table-column label="拥有角色" width="200" prop="role">
              <template slot-scope="scope">
                <div v-for="item in scope.row.roles" :key="item.id" :value="item">
                  <el-tag style="margin-bottom: 10px">{{item.roleName}}</el-tag>
                </div>
                
              </template>
            </el-table-column>
            <el-table-column label="登录IP" width="160" prop="lastLoginIp">
              <template slot-scope="scope">
                <span>{{ scope.row.lastLoginIp }}</span>
              </template>
            </el-table-column>

            <el-table-column label="最后登录时间" width="160" prop="lastLoginTime">
              <template slot-scope="scope">
                <span>{{ scope.row.lastLoginTime }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-if="checkPer(['admin', 'admin:edit', 'admin:del'])"
              label="操作"
              width="130px"
              align="center"
              fixed="right"
            >
              <template slot-scope="scope">
                <udOperation :data="scope.row" :permission="permission" :disabled-dle="scope.row.id === user.id"/>
              </template>
            </el-table-column>
          </el-table>
          <!--分页组件-->
          <pagination />
        </el-card>
    </el-row>
  </div>
</template>

<script>
import crudAdmins from "@/api/authority/admin";
import {getRoleByPage} from "@/api/authority/role";
import { isvalidPhone } from '@/utils/validate'
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import DateRangePicker from "@/components/DateRangePicker";
import { mapGetters } from 'vuex'
let userRoles = []
const defaultForm = { id: null, name: null, summary: null };
export default {
  name: "Admin",
  components: {
    Treeselect,
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "管理员",
      url: "/admin/manager/getAdminByPage",
      crudMethod: { ...crudAdmins },
      methodType: "post",
    });
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
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
      defaultProps: { children: "children", label: "label", isLeaf: "leaf" },
      currentId: 0,
      roleDatas: [],
      roles: [],
      imageUrl: '',
      permission: {
        add: ["admin", "admin:add"],
        edit: ["admin", "admin:edit"],
        del: ["admin", "admin:del"],
      },
      enabledTypeOptions: [
        { key: 'true', value: '激活' },
        { key: 'false', value: '禁用' }
      ],
      genderTypeOptions: [
        { key: 1, value: '男' },
        { key: 2, value: '女' }
      ],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        phone: [
          { required: true, trigger: 'blur', validator: validPhone }
        ]
      }
    };
  },
  methods: {
    // 新增前将多选的值设置为空
    [CRUD.HOOK.beforeToAdd]() {
      this.roleDatas = []
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (this.roleDatas.length === 0) {
        this.$message({
          message: '角色不能为空',
          type: 'warning'
        })
        return false
      }
      crud.form.roles = userRoles
      return true
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getRoles()
      if(form.enabled) {
          form.enabled = form.enabled.toString()
      }
    },
    // 新增前将多选的值设置为空
    [CRUD.HOOK.beforeToAdd]() {
      this.roleDatas = []
    },
    // 初始化编辑时候的角色与岗位
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.getJobs(this.form.dept.id)
      this.roleDatas = []
      userRoles = []
      const _this = this
      form.roles.forEach(function(role, index) {
        _this.roleDatas.push(role.id)
        const rol = { id: role.id }
        userRoles.push(rol)
      })
    },
    uploadURL() {
      return "";
    },
    handleAvatarSuccess(res, file) {
      console.log("file", file)
        this.imageUrl = URL.createObjectURL(file.raw);
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
     changeRole(value) {
      userRoles = []
      value.forEach(function(data, index) {
        const role = { id: data }
        userRoles.push(role)
      })
    },
    deleteTag(value) {
      userRoles.forEach(function(data, index) {
        if (data.id === value) {
          userRoles.splice(index, value)
        }
      })
    },
     // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
    },
    // 获取弹窗内角色数据
    getRoles() {
      const param = {searchAll:'true'}
      getRoleByPage(param).then(res => {
        if(res.code === this.$ECode.SUCCESS) {
          this.roles = res.data.data
        } else {
          this.$commonUtil.message.error(res.message);
        }
      }).catch(() => { })
    },
   
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + (val ? "激活" : "禁用") + '" ' + data.username + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudAdmins.edit(data).then(res => {
          this.crud.notify("" + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.role-span {
  font-weight: bold;
  color: #303133;
  font-size: 15px;
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
::v-deep .vue-treeselect__multi-value {
  margin-bottom: 0;
}
::v-deep .vue-treeselect__multi-value-item {
  border: 0;
  padding: 0;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  margin: 0, 0, 0, 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #06449b;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
