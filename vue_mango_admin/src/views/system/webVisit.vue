<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.behavior"
          size="small"
          clearable
          placeholder="输入用户行为搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <el-row :gutter="15">
      <!--分类管理-->
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span class="role-span">门户页面访问记录列表</span>
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
          <el-table-column
            :show-overflow-tooltip="true"
            prop="ip"
            label="IP"
            width="100"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="ipSource"
            label="IP来源"
            width="200"
          />
            <el-table-column
            :show-overflow-tooltip="true"
            prop="browser"
            label="浏览器"
            width="200"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="os"
            label="操作系统"
            width="100"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="behavior"
            label="用户行为"
            width="150"
          />
            <el-table-column
            :show-overflow-tooltip="true"
            prop="content"
            label="行为内容"
            width="200"
          />
            <el-table-column
            :show-overflow-tooltip="true"
            prop="requestTime"
            label="请求时间"
            width="200"
          />
          <el-table-column
            v-if="checkPer(['admin', 'webVisit:del'])"
            label="操作"
            width="130px"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <udOperation :data="scope.row" :permission="permission" :showEdit="false"/>
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
import crudWebVisit from "@/api/system/webVisit";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
const defaultForm = { id: null, behavior: null };
export default {
  name: "Comment",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "访问记录",
      url: "/admin/webVisit/getWebVisitByPage",
      crudMethod: { ...crudWebVisit },
      methodType: "post",
       optShow: {
      add: false,
      edit: false,
      del: true,
      download: false,
      reset: false
    },
    });
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      currentId: 0,
      permission: {
        del: ["admin", "comment:del"],
      },
    };
  },
  methods: {
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

