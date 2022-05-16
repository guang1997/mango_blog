<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.content"
          size="small"
          clearable
          placeholder="输入评论内容搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-input
          v-model="query.nickname"
          size="small"
          clearable
          placeholder="输入评论人昵称搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select
          v-model="query.type"
          clearable
          size="small"
          placeholder="评论类型"
          class="filter-item"
          style="width: 90px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in dict.sys_comment_type"
            :key="item.id"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
        <el-select
          v-model="query.source"
          clearable
          size="small"
          placeholder="评论来源"
          class="filter-item"
          style="width: 90px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in dict.sys_comment_source"
            :key="item.id"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <el-row :gutter="15">
      <!--分类管理-->
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span class="role-span">评论列表</span>
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
                :src="scope.row.avatar ? scope.row.avatar : Avatar"
                style="width: 100px; height: 100px"
              />
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="nickname"
            label="评论人"
            width="100"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="toNickname"
            label="被评论人"
            width="100"
          />
          <el-table-column label="评论类型" width="100">
            <template slot-scope="scope">
              <template>
                <el-tag
                  v-for="item in filterSysCommentType(scope.row.type)"
                  :type="item.listClass"
                  :key="item.id"
                  >{{ item.dictLabel }}</el-tag
                >
              </template>
            </template>
          </el-table-column>
          <el-table-column label="评论来源" width="150">
            <template slot-scope="scope">
              <template>
                <el-tag
                  v-for="item in filterSysCommentSource(scope.row.source)"
                  :type="item.listClass"
                  :key="item.id"
                  >{{ item.dictLabel }}</el-tag
                >
              </template>
            </template>
          </el-table-column>
          <el-table-column label="内容" width="300" prop="content" />
          <el-table-column label="创建时间" prop="createTime" />
          <el-table-column
            v-if="checkPer(['admin', 'comment:del'])"
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
import crudComment from "@/api/system/comment";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
import Avatar from "@/assets/images/avatar.png";
const defaultForm = { id: null, content: null };
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
      title: "评论",
      url: "/admin/comment/getCommentByPage",
      crudMethod: { ...crudComment },
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
  dicts: ["sys_comment_type", "sys_comment_source"],
  data() {
    return {
      currentId: 0,
      permission: {
        del: {
          roles:["admin"],
          menuButtons:["comment:del"]
        }
      },
      Avatar:Avatar
    };
  },
  computed: {
    filterSysCommentType() {
      return function (type) {
        return this.dict.sys_comment_type.filter(
          (item) => item.dictValue == type
        );
      };
    },
    filterSysCommentSource() {
      return function (source) {
        return this.dict.sys_comment_source.filter(
          (item) => item.dictValue == source
        );
      };
    },
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

