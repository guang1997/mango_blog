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
          <el-select
          v-model="query.commentStatus"
          clearable
          size="small"
          placeholder="评论状态"
          class="filter-item"
          style="width: 90px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in dict.sys_status"
            :key="item.id"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
    </div>
    <el-row :gutter="15">
      <!--分类管理-->
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span class="role-span">网站用户列表</span>
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
           <el-table-column label="头像" width="120" align="center">
            <template slot-scope="scope">
              <img
                :src="scope.row.avatar"
                style="width: 100px; height: 100px"
              />
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="username"
            label="用户名"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="nickname"
            label="昵称"
          />
        
           <el-table-column label="评论状态" align="center" prop="commentStatus" v-if="checkPer(['admin', 'user:edit'])">
            <template slot-scope="scope">
              <el-switch
                :value="scope.row.commentStatus"
                :active-value="1"
                :inactive-value="0"
                active-color="#409EFF"
                inactive-color="#F56C6C"
                @change="changeCommentStatus(scope.row, scope.row.commentStatus)"
              />
            </template>
          </el-table-column>
           <el-table-column label="登录次数" width="100" prop="loginCout">
            <template slot-scope="scope">
              <span>{{ scope.row.loginCount }}</span>
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
        </el-table>
        <!--分页组件-->
        <pagination />
      </el-card>
    </el-row>
  </div>
</template>

<script>
import crudUser from "@/api/authority/user";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
const defaultForm = {
  id: null,
  username: null,
  nickname: null,
  gender: 1,
  email: null,
  enabled: 1,
  phone: null,
  avatar: null,
  qqNumber: null,
  weChat: null,
  commentStatus: 1
};
export default {
  name: "User",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "网站用户",
      url: "/admin/user/getUserByPage",
      crudMethod: { ...crudUser },
      methodType: "post",
       optShow: {
      add: false,
      edit: true,
      del: false,
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
        edit: {
          roles:["admin"],
          menuButtons:["user:edit"]
        }
      },
    };
  },
  methods: {
     changeCommentStatus(data, val) {
      let oldCommentStatus = data.commentStatus;
      if (oldCommentStatus === 0) {
        data.commentStatus = 1;
      } else {
        data.commentStatus = 0;
      }
      this.$confirm(
        '此操作将 "' +
          (val === 0 ? "激活" : "禁用") +
          '" ' +
          data.username +
          "的评论, 是否继续？",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          crudUser
            .edit(data)
            .then((res) => {
              if (res.code === this.$ECode.SUCCESS) {
                this.crud.notify("" + "成功", CRUD.NOTIFICATION_TYPE.SUCCESS);
              } else {
                data.commentStatus = oldCommentStatus;
              }
            })
            .catch(() => {
              data.commentStatus = oldCommentStatus;
            });
        })
        .catch(() => {
          data.commentStatus = oldCommentStatus;
        });
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
.role-span {
  font-weight: bold;
  color: #303133;
  font-size: 15px;
}
</style>
