<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <el-row :gutter="15">
      <!--分类管理-->
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span class="role-span">异常日志列表</span>
        </div>
        <el-table ref="table" v-loading="crud.loading" style="width: 100%" :data="crud.data" @select="crud.selectChange"
          @select-all="crud.selectAllChange" @selection-change="crud.selectionChangeHandler">
          <el-table-column type="selection" width="55" />
          <el-table-column :show-overflow-tooltip="true" prop="ip" label="IP" width="100" />
          <el-table-column :show-overflow-tooltip="true" prop="ipSource" label="IP来源" width="150" />
          <el-table-column :show-overflow-tooltip="true" prop="method" label="接口名" width="150" />
          <el-table-column :show-overflow-tooltip="true" prop="exceptionMessage" label="错误信息" width="200" />
          <el-table-column :show-overflow-tooltip="true" prop="params" label="参数" width="200" />
          <el-table-column :show-overflow-tooltip="true" prop="createTime" label="报错时间" width="200" />
          <el-table-column label="操作" fixed="right" min-width="150">
            <template slot-scope="scope">
              <el-button @click="handleShow(scope.row)" type="primary" size="small">详情</el-button>
            </template>
          </el-table-column>ƒ
        </el-table>
        <!--分页组件-->
        <pagination />

        <el-dialog title="异常详情" :visible.sync="dialogVisible" :fullscreen="true">
          <h3>请求参数</h3>
          <span>{{ this.params }}</span>
          <h3>异常详情</h3>
          <span>{{ this.exceptionDetails }}}</span>
        </el-dialog>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import crudExceptionLog from "@/api/log/exceptionLog";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
const defaultForm = { id: null, behavior: null };
export default {
  name: "ExceptionLog",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "异常日志",
      url: "/admin/exceptionLog/getExceptionByPage",
      crudMethod: { ...crudExceptionLog },
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
      exceptionDetails: '',
      params: '',
      dialogVisible: false,
      permission: {
        del: {
          roles: ["admin"],
          menuButtons: ["exceptionLog:del"]
        }
      }
    };
  },
  methods: {
    handleShow: function (row) {
      this.exceptionDetails = row.exceptionDetails;
      this.params = row.params;
      this.dialogVisible = true;
    }
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

