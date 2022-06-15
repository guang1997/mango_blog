<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.tagName"
          size="small"
          clearable
          placeholder="输入标签名搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
          <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible="crud.status.cu > 0" :title="crud.status.title" width="580px">
          <el-form ref="form" :inline="true" :model="form" size="small" label-width="80px" :rules="rules">
            <el-form-item
              label="标签名称"
              prop="tagName"
            >
              <el-input
                v-model="form.tagName"
                placeholder="标签名称"
                style="width: 178px"
              />
            </el-form-item>
            <el-form-item label="描述信息" prop="summary">
              <el-input
                v-model="form.summary"
                style="width: 446px"
                rows="5"
                type="textarea"
              />
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
      <!--标签管理-->
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">标签列表</span>
          </div>
          <el-table
            ref="table"
            v-loading="crud.loading"
            style="width: 100%"
            :data="crud.data"
             @select="crud.selectChange"
             @select-all="crud.selectAllChange"
             @selection-change="crud.selectionChangeHandler"
          >
            <el-table-column type="selection" width="55" />

            <el-table-column :show-overflow-tooltip="true" prop="tagName" label="标签名" width="200"/>
            <el-table-column :show-overflow-tooltip="true" prop="summary" label="标签描述" width="300"/>
            <el-table-column label="点击数" width="150" prop="clickCount" />

            <el-table-column label="创建时间"  prop="createTime" />
            <el-table-column
              v-if="checkPer(['admin', 'tag:edit', 'tag:del'])"
              label="操作"
              width="130px"
              align="center"
              fixed="right"
            >
              <template slot-scope="scope">
                <udOperation :data="scope.row" :permission="permission"/>
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
import crudTags from "@/api/blog/tag";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
const defaultForm = { id: null, name: null, summary: null, sort: 999,};
export default {
  name: "Tag",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "标签",
      url: "/admin/tag/getTagByPage",
      crudMethod: { ...crudTags },
      methodType: "post",
    });
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      currentId: 0,
      permission: {
         add: {
          roles:["admin"],
          menuButtons:["tag:add"]
        },
        edit: {
          roles:["admin"],
          menuButtons:["tag:edit"]
        }, 
        del: {
          roles:["admin"],
          menuButtons:["tag:del"]
        }
      },
      rules: {
        tagName: [{ required: true, message: "请输入名称", trigger: "blur" }],
      },
    };
  },
  methods: {
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form || !crud.form.tagName) {
        this.$message({
          message: '角色名不能为空',
          type: 'warning'
        })
        return false
      }
      return true
    },
     // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
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

