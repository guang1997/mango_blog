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
          placeholder="输入名称或者描述搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker v-model="query.createTimes" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!-- 表单渲染 -->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible="crud.status.cu > 0"
      :title="crud.status.title"
      width="520px"
    >
      <el-form
        ref="form"
        :inline="true"
        :model="form"
        :rules="rules"
        size="small"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" style="width: 380px" />
        </el-form-item>
         <el-form-item label="角色级别" prop="level">
              <el-input-number
                v-model.number="form.level"
                :min="1"
                controls-position="right"
                style="width: 178px"
              />
            </el-form-item>
        <el-form-item label="描述信息" prop="summary">
          <el-input
            v-model="form.summary"
            style="width: 380px"
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
      <!--角色管理-->
      <el-col
        :xs="24"
        :sm="24"
        :md="16"
        :lg="16"
        :xl="17"
        style="margin-bottom: 10px"
      >
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">角色列表</span>
          </div>
          <el-table
            ref="table"
            v-loading="crud.loading"
            highlight-current-row
            style="width: 100%"
            :data="crud.data"
            @selection-change="crud.selectionChangeHandler"
            @current-change="handleCurrentChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="roleName" label="角色名称" />
            <el-table-column prop="level" label="角色级别" />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="summary"
              label="角色描述"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              width="150px"
              prop="createTime"
              label="创建日期"
            />
            <el-table-column
              v-if="checkPer(['admin', 'roles:edit', 'roles:del'])"
              label="操作"
              width="130px"
              align="center"
              fixed="right"
            >
              <template slot-scope="scope">
                <udOperation :data="scope.row" :permission="permission" />
              </template>
            </el-table-column>
          </el-table>
          <!--分页组件-->
          <pagination />
        </el-card>
      </el-col>
      <!-- 菜单授权 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="7">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip
              class="item"
              effect="dark"
              content="选择指定角色分配菜单"
              placement="top"
            >
              <span class="role-span">菜单分配</span>
            </el-tooltip>
            <el-button
              v-permission="permission.edit"
              :disabled="!showButton"
              :loading="menuLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 6px 9px"
              type="primary"
              @click="saveMenu"
              >保存</el-button
            >
          </div>
          <el-tree
            ref="menu"
            lazy
            :data="menus"
            :default-checked-keys="menuIds"
            :load="getMenuDatas"
            :props="defaultProps"
            check-strictly
            accordion
            show-checkbox
            node-key="id"
            @check="menuChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import crudRoles from "@/api/authority/role";
import { getMenusByPid, getChildren } from "@/api/authority/menu";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import DateRangePicker from "@/components/DateRangePicker";

const defaultForm = { id: null, name: null, summary: null, sort: 999, };
export default {
  name: "Role",
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
      title: "角色",
      url: "/admin/role/getRoleByPage",
      crudMethod: { ...crudRoles },
      methodType: "post",
    });
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      defaultProps: { children: "children", label: "label", isLeaf: "leaf" },
      currentId: 0,
      menuLoading: false,
      showButton: false,
      menus: [],
      menuIds: [],
      permission: {
        add: {
          roles:["admin"],
          menuButtons:["role:add"]
        },
        edit: {
          roles:["admin"],
          menuButtons:["role:edit"]
        }, 
        del: {
          roles:["admin"],
          menuButtons:["role:del"]
        }
      },
      rules: {
        roleName: [{ required: true, message: "请输入名称", trigger: "blur" }],
      },
    };
  },
  methods: {
    getMenuDatas(node, resolve) {
      setTimeout(() => {
        getMenusByPid(node.data.id ? node.data.id : 0).then((res) => {
          if (res.code === this.$ECode.SUCCESS) {
            resolve(res.data.data);
          } else {
            this.$commonUtil.message.error(res.message);
          }
        });
      }, 100);
    },
    [CRUD.HOOK.afterRefresh]() {
      this.$refs.menu.setCheckedKeys([]);
    },
    // 新增前初始化信息
    [CRUD.HOOK.beforeToAdd](crud, form) {
      form.menus = null;
    },
    // 触发单选
    handleCurrentChange(val) {
      if (val) {
        const _this = this;
        // 清空菜单的选中
        this.$refs.menu.setCheckedKeys([]);
        // 保存当前的角色id
        this.currentId = val.id;
        // 初始化默认选中的key
        this.menuIds = [];
        val.menus.forEach(function (data) {
          _this.menuIds.push(data.id);
        });
        this.showButton = true;
      }
    },
    menuChange(menu) {
      // 获取该节点的所有子节点，id 包含自身
      getChildren(menu.id).then(res => {
        // 判断是否在 menuIds 中，如果存在则删除，否则添加
        const childIds = res.data;
        if (this.menuIds.indexOf(menu.id) !== -1) {
          for (let i = 0; i < childIds.length; i++) {
            const index = this.menuIds.indexOf(childIds[i]);
            if (index !== -1) {
              this.menuIds.splice(index, 1);
            }
          }
        } else {
          for (let i = 0; i < childIds.length; i++) {
            const index = this.menuIds.indexOf(childIds[i]);
            if (index === -1) {
              this.menuIds.push(childIds[i]);
            }
          }
        }
        this.$refs.menu.setCheckedKeys(this.menuIds);
      });
    },
    // 保存菜单
    saveMenu() {
      this.menuLoading = true;
      const role = { id: this.currentId, menus: [] };
      // 得到已选中的 key 值
      this.menuIds.forEach(function (id) {
        const menu = { id: id };
        role.menus.push(menu);
      });
      crudRoles
        .editMenu(role)
        .then((res) => {
           if (res.code === this.$ECode.SUCCESS) {
            this.crud.notify("保存成功", CRUD.NOTIFICATION_TYPE.SUCCESS);
            this.menuLoading = false;
            this.update();
          } else {
            this.$commonUtil.message.error(res.message);
          }
          
        })
        .catch((err) => {
          this.menuLoading = false;
          // console.log(err.response.data.message);
        });
    },
    // 改变数据
    update() {
      // 无刷新更新 表格数据
      crudRoles.getRoleById(this.currentId).then((res) => {
        for (let i = 0; i < this.crud.data.length; i++) {
            if (res.data.id === this.crud.data[i].id) {
              this.crud.data[i] = res.data.data
              break;
            }
          }
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
</style>
