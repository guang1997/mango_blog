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
          placeholder="输入标题或描述搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-input
          v-model="query.url"
          size="small"
          clearable
          placeholder="输入网站地址搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select v-model="query.linkStatus" clearable size="small" placeholder="状态" class="filter-item" style="width: 90px" @change="crud.toQuery">
          <el-option v-for="item in dict.sys_link_status" :key="item.id" :label="item.dictLabel" :value="item.dictValue" />
        </el-select>
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
          <!--表单渲染-->
        <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="580px">
          <el-form ref="form" :inline="true" :model="form" size="small" label-width="80px" :rules="rules">
            <el-form-item
              label="网站标题"
              prop="title"
            >
              <el-input
                v-model="form.title"
                placeholder="请输入友链标题"
                style="width: 446px"
              />
            </el-form-item>
            <el-form-item label="友链url" prop="url">
               <el-input
                v-model="form.url"
                placeholder="请输入友链url"
                style="width: 446px"
              />
            </el-form-item>
             <el-form-item label="友链图标" prop="fileId">
               <el-input
                v-model="form.fileId"
                placeholder="请输入友链图标"
                style="width: 446px"
              />
            </el-form-item>
            <el-form-item label="友链介绍" prop="summary">
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
      <!--分类管理-->
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">友链列表</span>
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
            <el-table-column label="友链图标" width="120" align="center">
              <template slot-scope="scope">
                <img
                  :src="scope.row.fileId"
                  style="width: 100px;height: 100px;"
                >
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="title" label="友链标题" width="150"/>
             <el-table-column :show-overflow-tooltip="true" prop="url" label="友链url" width="200"/>
            <el-table-column :show-overflow-tooltip="true" prop="summary" label="友链描述" width="250"/>
            <el-table-column label="友链状态" width="200">
              <template slot-scope="scope">
                <template>
                  <el-tag v-for="item in filterSysLinkStatus(scope.row.linkStatus)" 
                  :type="item.listClass" :key="item.id">{{item.dictLabel}}</el-tag>
                </template>
              </template>
            </el-table-column>

            <el-table-column label="创建时间"  prop="createTime" />
            <el-table-column
              v-if="checkPer(['admin', 'sort:edit', 'sort:del'])"
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
import crudLink from "@/api/system/link";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
const defaultForm = { id: null, name: null, summary: null, linkStatus: 0};
export default {
  name: "Sort",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "友情链接",
      url: "/admin/link/getLinkByPage",
      crudMethod: { ...crudLink },
      methodType: "post",
    });
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts:['sys_link_status'],
  data() {
    return {
      currentId: 0,
      permission: {
        add: ["admin", "link:add"],
        edit: ["admin", "link:edit"],
        del: ["admin", "link:del"],
      },
      rules: {
        url: [{ required: true, message: "请输入网站地址", trigger: "blur" }],
      },
    };
  },
  computed: {
    filterSysLinkStatus() {
      return function(linkStatus) {
        return this.dict.sys_link_status.filter(item => item.dictValue == linkStatus)
      }
    }
  },
  methods: {
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form || !crud.form.url) {
        this.$message({
          message: '网站地址不能为空',
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

