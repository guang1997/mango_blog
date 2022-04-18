<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.title"
          size="small"
          clearable
          placeholder="输入博客标题搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
          <el-input
          v-model="query.author"
          size="small"
          clearable
          placeholder="输入作者搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        
        <el-select
          v-model="query.blogSortId"
          clearable
          size="small"
          placeholder="分类"
          class="filter-item"
          style="width: 150px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in sorts"
            :key="item.id"
            :label="item.sortName"
            :value="item.id"
          />
        </el-select>
        
        <el-select
          v-model="query.tagId"
          clearable
          size="small"
          placeholder="标签"
          class="filter-item"
          style="width: 150px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in tags"
            :key="item.id"
            :label="item.tagName"
            :value="item.id"
          />
        </el-select>
        <el-select
          v-model="query.openComment"
          clearable
          size="small"
          placeholder="是否开启评论"
          class="filter-item"
          style="width: 150px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in dict.sys_yes_no"
            :key="item.id"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible="crud.status.cu > 0"
      :title="crud.status.title"
      width="570px"
    >
      <el-form
        ref="form"
        :inline="true"
        :model="form"
        :rules="rules"
        size="small"
        label-width="66px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model.number="form.phone" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
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
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.enabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item style="margin-bottom: 0" label="角色" prop="roles">
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

        <el-form-item label="头像">
          <div style="text-align: center">
            <div class="el-upload">
              <img
                :src="fileId"
                title="点击上传博客首页图"
                class="fileId"
                @click="toggleShow"
                style="margin-top: 20px"
              />
              <myUpload
                :modelValue.sync="show"
                field="file"
                :headers="headers"
                :url="imagesUploadApi"
                :params="{ moduleName: 'avatar' }"
                :size="size"
                @crop-upload-success="cropUploadSuccess"
              />
            </div>
          </div>
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
      <!--博客管理-->
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span class="role-span">博客列表</span>
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
          <el-table-column label="标题图" width="120" align="center">
            <template slot-scope="scope">
              <img
                :src="scope.row.fileId"
                style="width: 100px; height: 100px"
              />
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="title"
            label="标题"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="author"
            label="作者"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="sortName"
            label="分类"
          />
          <el-table-column label="标签" width="200" prop="role">
            <template slot-scope="scope">
              <div v-for="item in scope.row.tags" :key="item.id" :value="item">
                <el-tag style="margin-bottom: 10px">{{ item.tagName }}</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="推荐等级" width="100">
            <template slot-scope="scope">
              <template>
                <el-tag
                  v-for="item in filterSysLevel(scope.row.level)"
                  :type="item.listClass"
                  :key="item.id"
                  >{{ item.dictLabel }}</el-tag
                >
              </template>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="clickCount"
            label="点击数"
          />
           <el-table-column
            :show-overflow-tooltip="true"
            prop="likeCount"
            label="喜欢数"
          />
      <el-table-column label="开启评论" width="100">
            <template slot-scope="scope">
              <template>
                <el-tag
                  v-for="item in filterSysYesNo(scope.row.openComment)"
                  :type="item.listClass"
                  :key="item.id"
                  >{{ item.dictLabel }}</el-tag
                >
              </template>
            </template>
          </el-table-column>
          <el-table-column label="创建时间"  prop="createTime" width="160"/>
          <el-table-column
            v-if="checkPer(['admin', 'blog:edit', 'blog:del'])"
            label="操作"
            width="130px"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
                :disabled-dle="scope.row.id === user.id"
              />
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
import crudBlogs from "@/api/blog/blog";
import {getSortByPage} from "@/api/blog/sort";
import {getTagByPage} from "@/api/blog/tag";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
import { mapGetters } from "vuex";
import myUpload from "vue-image-crop-upload";
let userRoles = [];
const defaultForm = {
  id: null,
  username: null,
  nickname: null,
  gender: 1,
  email: null,
  enabled: 1,
  roles: [],
  phone: null,
  qqNumber: null,
  weChat: null,
  sorts:[],
  tags:[]
};
export default {
  name: "Blog",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
    myUpload,
  },
  cruds() {
    return CRUD({
      title: "博客",
      url: "/admin/blog/getBlogByPage",
      crudMethod: { ...crudBlogs },
      methodType: "post",
    });
  },
  computed: {
    ...mapGetters(["user", "imagesUploadApi"]),
    filterSysLevel() {
      return function (level) {
        return this.dict.sys_blog_level.filter((item) => item.dictValue == level);
      };
    },
    filterSysYesNo() {
      return function(value) {
        return this.dict.sys_yes_no.filter((item) => item.dictValue == value)
      }
    }
  },
  created() {
      getTagByPage({queryAll:true}).then(res => {
      if (res.code === this.$ECode.SUCCESS) {
        this.tags = res.data.data;
      }
      
    })
     getSortByPage({queryAll:true}).then(res => {
      if (res.code === this.$ECode.SUCCESS) {
        this.sorts = res.data.data;
      }
      
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ["sys_blog_level", "sys_yes_no"],
  data() {
  
    return {
      defaultProps: { children: "children", label: "label", isLeaf: "leaf" },
      currentId: 0,
      permission: {
        add: ["admin", "blog:add"],
        edit: ["admin", "blog:edit"],
        del: ["admin", "blog:del"],
      },
      rules: {
        
      },
      show: false,
      size: 2,
    };
  },
  methods: {
    // 新增前将多选的值设置为空
    [CRUD.HOOK.beforeToAdd]() {
      this.roleDatas = [];
    },
    // 添加取消之后
    [CRUD.HOOK.afterAddCancel]() {
      this.userAvatar = null;
    },
    // 编辑取消之后
    [CRUD.HOOK.afterEditCancel]() {
      this.userAvatar = null;
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (this.roleDatas.length === 0) {
        this.$message({
          message: "角色不能为空",
          type: "warning",
        });
        return false;
      }
      crud.form.roles = userRoles;
      crud.form.avatar = this.userAvatar;
      return true;
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getRoles();
      if (form.avatar) {
        this.userAvatar = form.avatar;
      }
    },
    // 新增前将多选的值设置为空
    [CRUD.HOOK.beforeToAdd]() {
      this.roleDatas = [];
      this.userAvatar = null;
    },
    // 初始化编辑时的角色
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.roleDatas = [];
      userRoles = [];
      const _this = this;
      form.roles.forEach(function (role, index) {
        _this.roleDatas.push(role.id);
        const rol = { id: role.id };
        userRoles.push(rol);
      });
    },
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false;
      }
    },
  
    toggleShow() {
      this.show = !this.show;
    },
    cropUploadSuccess(jsonData, field) {

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
.fileId {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}
</style>
