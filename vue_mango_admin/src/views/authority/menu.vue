<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible="crud.status.cu > 0"
      :title="crud.status.title"
      width="580px"
    >
      <el-form
        ref="form"
        :inline="true"
        :model="form"
        :rules="rules"
        size="small"
        label-width="80px"
      >
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group
            v-model="form.menuType"
            size="mini"
            style="width: 178px"
          >
            <el-radio-button label="0">目录</el-radio-button>
            <el-radio-button label="1">菜单</el-radio-button>
            <el-radio-button label="2">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-show="form.menuType.toString() !== '2'"
          label="菜单图标"
          prop="icon"
        >
          <el-popover
            placement="bottom-start"
            width="450"
            trigger="click"
            @show="$refs['iconSelect'].reset()"
          >
            <IconSelect ref="iconSelect" @selected="selected" />
            <el-input
              slot="reference"
              v-model="form.icon"
              style="width: 450px"
              placeholder="点击选择图标"
              readonly
            >
              <svg-icon
                v-if="form.icon"
                slot="prefix"
                :icon-class="form.icon"
                class="el-input__icon"
                style="height: 32px; width: 16px"
              />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item
          v-show="form.menuType.toString() !== '2'"
          label="菜单可见"
          prop="hidden"
        >
          <el-radio-group v-model="form.hidden" size="mini">
            <el-radio-button label="false">是</el-radio-button>
            <el-radio-button label="true">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-if="form.menuType.toString() !== '2'"
          label="菜单标题"
          prop="title"
        >
          <el-input
            v-model="form.title"
            :style="
              form.menuType.toString() === '0' ? 'width: 450px' : 'width: 178px'
            "
            placeholder="菜单标题"
          />
        </el-form-item>
        <el-form-item
          v-if="form.menuType.toString() === '2'"
          label="按钮名称"
          prop="title"
        >
          <el-input
            v-model="form.title"
            placeholder="按钮名称"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item
          v-show="form.menuType.toString() !== '0'"
          label="权限标识"
          prop="permission"
        >
          <el-input
            v-model="form.permission"
            placeholder="权限标识, 如menu:list"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item
          v-if="form.menuType.toString() !== '2'"
          label="路由地址"
          prop="path"
        >
          <el-input
            v-model="form.path"
            placeholder="路由地址，即path"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item label="菜单排序" prop="menuSort">
          <el-input-number
            v-model.number="form.sort"
            :min="0"
            :max="999"
            controls-position="right"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item
          v-show="form.menuType.toString() === '1'"
          label="组件名称"
          prop="name"
        >
          <el-input
            v-model="form.name"
            style="width: 178px"
            placeholder="匹配组件内Name字段"
          />
        </el-form-item>
        <el-form-item
          v-show="form.menuType.toString() === '1'"
          label="组件路径"
          prop="component"
        >
          <el-input
            v-model="form.component"
            style="width: 178px"
            placeholder="组件路径, 即component"
          />
        </el-form-item>
        <el-form-item label="上级类目" prop="pid">
          <treeselect
            v-model="form.pid"
            :options="menus"
            :load-options="loadMenus"
            style="width: 450px"
            placeholder="选择上级类目"
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
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      lazy
      :load="getMenus"
      :data="crud.data"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      row-key="id"
      @select="crud.selectChange"
      @select-all="crud.selectAllChange"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        :show-overflow-tooltip="true"
        label="菜单标题"
        width="200px"
        prop="title"
      />
      <el-table-column prop="icon" label="图标" align="center" width="60px">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
        </template>
      </el-table-column>
      <el-table-column prop="sort" align="center" label="排序">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="permission"
        label="权限标识"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="component"
        label="组件路径"
      />

      <el-table-column prop="hidden" label="可见" width="75px">
        <template slot-scope="scope">
          <span v-if="scope.row.hidden">否</span>
          <span v-else>是</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建日期" width="200px" />
      <el-table-column
        v-if="checkPer(['admin', 'menu:edit', 'menu:del'])"
        label="操作"
        width="130px"
        align="center"
        fixed="right"
      >
        -->
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
            msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import crudMenu from "@/api/authority/menu";
import IconSelect from "@/components/IconSelect";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { LOAD_CHILDREN_OPTIONS } from "@riophae/vue-treeselect";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import DateRangePicker from "@/components/DateRangePicker";

// crud交由presenter持有
const defaultForm = {
  id: null,
  title: null,
  sort: 999,
  path: null,
  component: null,
  name: null,
  roles: [],
  pid: 0,
  icon: null,
  hidden: false,
  menuType: 0,
  permission: null,
};
export default {
  name: "Menu",
  components: {
    Treeselect,
    IconSelect,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "菜单",
      url: "/admin/menu/getMenusByPid",
      crudMethod: { ...crudMenu },
      page: { page: undefined, size: undefined },
    });
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      menus: [],
      permission: {
        add: {
          roles:["admin"],
          menuButtons:["menu:add"]
        },
        edit: {
          roles:["admin"],
          menuButtons:["menu:edit"]
        }, 
        del: {
          roles:["admin"],
          menuButtons:["menu:del"]
        }
      },
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
        path: [{ required: true, message: "请输入地址", trigger: "blur" }],
      },
    };
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.menus = [];
      if (form.id != null) {
        if (form.pid === null) {
          form.pid = 0;
        }
        this.getSuperior(form.id);
      } else {
        this.menus.push({ id: 0, label: "顶级类目", children: null });
      }
    },
    getMenus(tree, treeNode, resolve) {
      setTimeout(() => {
        crudMenu.getMenusByPid(tree.id).then((res) => {
          if (res.code === this.$ECode.SUCCESS) {
            resolve(res.data.data);
          } else {
            this.$commonUtil.message.error(res.message);
          }
        });
      }, 100);
    },
    getSuperior(id) {
      crudMenu.getSuperior(id).then((res) => {
        if (res.code === this.$ECode.SUCCESS) {
          const children = res.data.map(function (obj) {
            if (obj.leaf) {
              delete obj.children;
            }
            return obj;
          });
          this.menus = [{ id: 0, label: "顶级类目", children: children }];
        } else {
          this.$commonUtil.message.error(res.message);
        }
      });
    },
    loadMenus({ action, parentNode, callback }) {
      if (action === LOAD_CHILDREN_OPTIONS) {
        crudMenu.getMenusByPid(parentNode.id).then((res) => {
          if (res.code === this.$ECode.SUCCESS) {
            parentNode.children = res.data.data.map(function (obj) {
            if (obj.leaf) {
              delete obj.children;
            }
            return obj;
          });
          setTimeout(() => {
            callback();
          }, 100);
          } else {
            this.$commonUtil.message.error(res.message);
          }
        });
      }
    },
    // 选中图标
    selected(name) {
      this.form.icon = name;
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
::v-deep .vue-treeselect__control,
::v-deep .vue-treeselect__placeholder,
::v-deep .vue-treeselect__single-value {
  height: 30px;
  line-height: 30px;
}
</style>
