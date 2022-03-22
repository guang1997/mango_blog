<template>
  <div>
    <div v-if="query.dictId === ''">
      <div class="my-code">点击字典查看详情</div>
    </div>
    <div v-else>
      <!--工具栏-->
      <div class="head-container">
      </div>
      <!--表单组件-->
      <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px"  :inline="true">
          <el-form-item label="字典标签" prop="dictLabel">
            <el-input v-model="form.dictLabel" style="width: 140px;" />
          </el-form-item>
          <el-form-item label="字典值" prop="dictValue">
            <el-input v-model="form.dictValue" style="width: 140px;" />
          </el-form-item>
          <el-form-item label="回显样式" prop="listClass">
          <el-select v-model="form.listClass" style="width: 140px;" clearable placeholder="请选择回显样式">
            <el-option
              v-for="item in listClassType"
              :key="item.key"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="扩展样式" prop="cssClass">
          <el-input v-model="form.cssClass" style="width: 140px;" auto-complete="off"></el-input>
        </el-form-item>
         <el-form-item label="是否默认" prop="isDefault">
              <el-radio-group v-model="form.isDefault" style="width: 140px">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model.number="form.sort" :min="0" :max="999" controls-position="right" style="width: 140px;" />
          </el-form-item>
          <el-form-item label="描述信息" prop="summary">
              <el-input
                v-model="form.summary"
                style="width: 370px"
                rows="5"
                type="textarea"
              />
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column label="所属字典">
          <el-tag>{{ query.dictName }}</el-tag>
        </el-table-column>
        <el-table-column prop="dictLabel" label="字典标签" />
        <el-table-column prop="dictValue" label="字典值" width="80"/>
         <el-table-column prop="dictValue" label="是否默认" width="80">
            <template slot-scope="scope">
              <el-tag type="success" v-if="scope.row.isDefault===true">是</el-tag>
              <el-tag type="warning" v-if="scope.row.isDefault===false">否</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="回显样式" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.listClass == 'default'">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'success'" type="success">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'primary'" type="primary">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'info'" type="info">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'warning'" type="warning">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'danger'" type="danger">{{scope.row.listClass}}</el-tag>
        </template>
      </el-table-column>
        <el-table-column prop="sort" label="排序" width="50"/>
         <el-table-column :show-overflow-tooltip="true" prop="summary" label="描述" />
        <el-table-column v-if="checkPer(['admin','dict:edit','dict:del'])" label="操作" width="130px" align="center" fixed="right">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudDictDetail from '@/api/system/dictDetail'
import CRUD, { presenter, header, form } from '@/components/Crud/crud'
import pagination from '@/components/Crud/Pagination'
import rrOperation from '@/components/Crud/RR.operation'
import udOperation from '@/components/Crud/UD.operation'

const defaultForm = { id: null, dictLabel: null, dictValue: null, sort: 999 }

export default {
  components: { pagination, rrOperation, udOperation },
  cruds() {
    return [
      CRUD({ title: '字典详情', url: '/admin/dictDetail/getDictDetailByPage', query: { dictId: '' }, sort: ['sort,desc'],
        crudMethod: { ...crudDictDetail },
        optShow: {
          add: true,
          edit: true,
          del: true,
          reset: false
        },
        queryOnPresenterCreated: false,
         methodType: "post",
      })
    ]
  },
  mixins: [
    presenter(),
    header(),
    form(function() {
      return Object.assign(defaultForm)
    })],
  data() {
    return {
      // dictId: null,
      // dictName: null,
      rules: {
        dictLabel: [
          { required: true, message: '请输入字典标签', trigger: 'blur' }
        ],
        dictValue: [
          { required: true, message: '请输入字典值', trigger: 'blur' }
        ]
      },
      permission: {
        add: ['admin', 'dict:add'],
        edit: ['admin', 'dict:edit'],
        del: ['admin', 'dict:del']
      },
      listClassType: [
          {key: 1, label: "default", value: 'default'},
          {key: 2, label: "primary", value: 'primary'},
          {key: 3, label: "success", value: 'success'},
          {key: 4, label: "info", value: 'info'},
          {key: 5, label: "warning", value: 'warning'},
          {key: 6, label: "danger", value: 'danger'}
        ],
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
