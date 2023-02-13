<template>
    <div class="app-container">
      <el-tabs type="border-card">
        <el-tab-pane>
          <span slot="label">
            <i class="el-icon-date"></i> 网站信息
          </span>
          <el-form style="margin-left: 20px;" label-position="left" :model="configForm" label-width="80px" ref="from">
  
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="网站名称">
                  <el-input v-model="configForm.name" placeholder="请输入网站名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作者">
                  <el-input v-model="configForm.author" placeholder="请输入作者"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
  
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="备案号">
                  <el-input v-model="configForm.recordNum" placeholder="请输入备案号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="github">
                  <el-input v-model="configForm.github" placeholder="请输入github地址"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="gitee">
                  <el-input v-model="configForm.gitee" placeholder="请输入gitee地址"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="描述">
                  <el-input :rows="10" type="textarea" v-model="configForm.summary" placeholder="请输入描述"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <el-button v-if="checkPer(['admin', 'webConfig:add'])" type="primary" @click="submitForm()">保 存</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
  
        <el-tab-pane>
          <template>
            <el-button @click="openAdd" class="filter-item" size="mini" type="primary" icon="el-icon-plus">新增</el-button>
          </template>
  
          <span slot="label">
            <i class="el-icon-date"></i> 首页滚动句子
          </span>
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="name" label="句子">
            </el-table-column>
            <el-table-column v-if="checkPer(['admin', 'webConfig:add', 'webConfig:del'])" label="操作" width="130px"
              align="center">
              <template slot-scope="scope">
                <el-button :permission="permission.add" size="mini" type="primary" icon="el-icon-edit"
                  @click="openEdit(scope.$index, scope.row)" />
  
                <el-popover :permission="permission.del" placement="top" width="180" 
                  :ref="`popover-${scope.$index}`">
                  <p>确定删除本条数据吗？</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="mini" type="text" @click="scope._self.$refs[`popover-${scope.$index}`].doClose()">取消</el-button>
                    <el-button type="primary" size="mini" @click="delRollingSentence(scope.row),scope._self.$refs[`popover-${scope.$index}`].doClose()">确定</el-button>
                  </div>
                  <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini" />
                </el-popover>
              </template>
            </el-table-column>
  
          </el-table>
        </el-tab-pane>
      </el-tabs>
  
    </div>
  </template>
  
  <script>
  import webConfigApi from '@/api/system/webConfig'
  export default {
    name: 'WebConfig',
    data() {
      return {
        configForm: {
          name: "",
          author: "",
          summary: "",
          recordNum: "",
          github: "",
          gitee: "",
          rollingSentences: []
        },
        tableData: [],
        sentence: "",
        permission: {
          add: {
            roles: ["admin"],
            menuButtons: ["webConfig:add"]
          },
          edit: {
            roles: ["admin"],
            menuButtons: ["webConfig:del"]
          }
        }
      }
    },
    created() {
      this.getWebConfig()
    },
    methods: {
      openAdd() {
        this.$prompt('请输入句子', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^[^#]*$/,
          inputErrorMessage: '句子格式不正确'
        }).then(({ value }) => {
          this.addRollingSentences(value)
        }).catch(() => {
        });
      },
      openEdit(index, data) {
        this.$prompt('请输入句子', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: data.name,
          inputPattern:  /^[^#]*$/,
          inputErrorMessage: '句子格式不正确'
        }).then(({ value }) => {
          this.editRollingSentences(index, value)
        }).catch(() => {
        });
      },
      submitForm() {
        webConfigApi.edit(this.configForm).then(res => {
          if (res.code == 20000) {
            this.$message.success("保存成功")
          } else {
            this.$message.error(res.message)
          }
        })
      },
      editRollingSentences(index, sentence) {
        this.configForm.rollingSentences[index] = sentence
        webConfigApi.edit(this.configForm).then(res => {
          if (res.code == 20000) {
            this.$message.success("保存成功")
            // this.tableData[index].name = sentence
            this.getWebConfig()
          } else {
            this.$message.error(res.message)
          }
        })
      },
      addRollingSentences(sentence) {
        if (!this.configForm.rollingSentences) {
          this.configForm.rollingSentences = []
        }
        this.configForm.rollingSentences.push(sentence)
        webConfigApi.edit(this.configForm).then(res => {
          if (res.code == 20000) {
            this.$message.success("保存成功")
            this.getWebConfig()
            // this.tableData.push({name:sentence})
          } else {
            this.$message.error(res.message)
          }
        })
      },
      delRollingSentence(data) {
        var deleteForm = this.configForm;
        deleteForm.rollingSentences = [data.name];
        deleteForm.deleteSentence = true;
        webConfigApi.edit(deleteForm).then(res => {
          if (res.code == 20000) {
            this.$message.success("删除成功")
            this.getWebConfig()
          } else {
            this.$message.error(res.message)
          }
        })
      },
      toDelete(index) {
        this.$nextTick(() => {
          // 因为会展示多个，添加此代码以过滤
          document.getElementById(this.$refs['popover-' + index].$refs.popper.id).style.display = 'none'
        })
      },
      getWebConfig() {
        webConfigApi.getWebConfig().then(res => {
        if (res.code == 20000 && res.data) {
          this.configForm = res.data
          var sentences = res.data.rollingSentences;
          this.tableData = []
          if (sentences) {
            for (var i = 0; i < sentences.length; i++) {
              var sentence = { name: sentences[i] }
              this.tableData.push(sentence)
            }
          }
        }
      })
      }
    }
  }
  </script>
  
  <style scoped>
  
  </style>
  