<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.title"
          size="small"
          clearable
          placeholder="输入博客标题搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-input
          v-model="query.author"
          size="small"
          clearable
          placeholder="输入作者搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />

        <el-select
          v-model="query.blogSortId"
          clearable
          size="small"
          placeholder="分类"
          class="filter-item"
          style="width: 150px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in sorts"
            :key="item.id"
            :label="item.sortName"
            :value="item.id"
          />
        </el-select>

        <el-select
          v-model="query.tagId"
          clearable
          size="small"
          placeholder="标签"
          class="filter-item"
          style="width: 150px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in tags"
            :key="item.id"
            :label="item.tagName"
            :value="item.id"
          />
        </el-select>
        <el-select
          v-model="query.openComment"
          clearable
          size="small"
          placeholder="是否开启评论"
          class="filter-item"
          style="width: 150px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in dict.sys_yes_no"
            :key="item.id"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible="crud.status.cu > 0"
      :title="crud.status.title"
      fullscreen
    >
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="标题图" :label-width="formLabelWidth">
          <el-upload
            class="avatar-uploader"
            :action="imagesUploadApi + '?moduleName=blog'"
            :show-file-list="false"
            :on-success="handleFileSuccess"
            :headers="headers"
            :before-upload="beforeFileUpload">
            <img v-if="fileId" :src="fileId" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <el-form-item label="标题" :label-width="formLabelWidth" prop="title">
          <el-input
            v-model="form.title"
            auto-complete="off"
            @input="contentChange"
          ></el-input>
        </el-form-item>

        <el-form-item label="简介" :label-width="formLabelWidth">
          <el-input
            v-model="form.summary"
            auto-complete="off"
            @input="contentChange"
          ></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="4">
            <el-form-item
              label="分类"
              :label-width="formLabelWidth"
              prop="blogSortUid"
            >
              <el-select
                v-model="form.blogSortId"
                size="small"
                placeholder="请选择"
                style="width: 150px"
              >
                <el-option
                  v-for="item in sorts"
                  :key="item.id"
                  :label="item.sortName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="标签" label-width="80px">
              <el-select
                v-model="tagDatas"
                multiple
                size="small"
                placeholder="请选择"
                style="width: 150px"
                @remove-tag="deleteTag"
                @change="changeTag"
              >
                <el-option
                  v-for="item in tags"
                  :key="item.id"
                  :label="item.tagName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="推荐等级" prop="level">
              <el-select
                v-model="form.level"
                size="small"
                placeholder="请选择"
                style="width: 150px"
              >
                <el-option
                  v-for="item in dict.sys_blog_level"
                  :key="item.id"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="网站评论">
              <el-radio-group v-model="form.openComment">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item
          label="作者"
          :label-width="formLabelWidth"
          v-if="form.isOriginal == 0"
          prop="author"
        >
          <el-input v-model="form.author" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item
          label="文章出处"
          :label-width="formLabelWidth"
          v-if="form.isOriginal == 0"
        >
          <el-input v-model="form.articlesPart" auto-complete="off"></el-input>
        </el-form-item>

        <!-- <el-form-item label="内容" :label-width="formLabelWidth" prop="content">
          <CKEditor ref="ckeditor" :content="form.content" @contentChange="contentChange" :height="320"></CKEditor>
        </el-form-item> -->
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
      <!--博客管理-->
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <span class="role-span">博客列表</span>
        </div>
        <el-table
          ref="table"
          v-loading="crud.loading"
          highlight-current-row
          style="width: 100%"
          :data="crud.data"
          @selection-change="crud.selectionChangeHandler"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="标题图" width="120" align="center">
            <template slot-scope="scope">
              <img
                :src="scope.row.fileId"
                style="width: 100px; height: 100px"
              />
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="title"
            label="标题"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="author"
            label="作者"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="sortName"
            label="分类"
          />
          <el-table-column label="标签" width="100" prop="role">
            <template slot-scope="scope">
              <div v-for="item in scope.row.tags" :key="item.id" :value="item">
                <el-tag style="margin-bottom: 10px">{{ item.tagName }}</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="推荐等级" width="100">
            <template slot-scope="scope">
              <template>
                <el-tag
                  v-for="item in filterSysLevel(scope.row.level)"
                  :type="item.listClass"
                  :key="item.id"
                  >{{ item.dictLabel }}</el-tag
                >
              </template>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="clickCount"
            label="点击数"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="likeCount"
            label="喜欢数"
          />
          <el-table-column label="开启评论" width="100">
            <template slot-scope="scope">
              <template>
                <el-tag
                  v-for="item in filterSysYesNo(scope.row.openComment)"
                  :type="item.listClass"
                  :key="item.id"
                  >{{ item.dictLabel }}</el-tag
                >
              </template>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="createTime" width="160" />
          <el-table-column
            v-if="checkPer(['admin', 'blog:edit', 'blog:del'])"
            label="操作"
            width="130px"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <udOperation
                :data="scope.row"
                :permission="permission"
                :disabled-dle="scope.row.id === user.id"
              />
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
import crudBlogs from "@/api/blog/blog";
import { getSortByPage } from "@/api/blog/sort";
import { getTagByPage } from "@/api/blog/tag";
import CRUD, { presenter, header, form, crud } from "@/components/Crud/crud";
import rrOperation from "@/components/Crud/RR.operation";
import crudOperation from "@/components/Crud/CRUD.operation";
import udOperation from "@/components/Crud/UD.operation";
import pagination from "@/components/Crud/Pagination";
import DateRangePicker from "@/components/DateRangePicker";
import { mapGetters } from "vuex";
import { getToken } from '@/utils/auth';
let blogTags = [];
const defaultForm = {
  id: null,
  title: null,
  summary: null,
  clickCount: 0,
  likeCount: 0,
  fileId: null,
  adminId: null,
  author: null,
  blogSortId: null,
  level: null,
  sort: 999,
  openComment: 1,
  fileId: null,
};
export default {
  name: "Blog",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
  },
  cruds() {
    return CRUD({
      title: "博客",
      url: "/admin/blog/getBlogByPage",
      crudMethod: { ...crudBlogs },
      methodType: "post",
    });
  },
  computed: {
    ...mapGetters(["user", "imagesUploadApi"]),
    filterSysLevel() {
      return function (level) {
        return this.dict.sys_blog_level.filter(
          (item) => item.dictValue == level
        );
      };
    },
    filterSysYesNo() {
      return function (value) {
        return this.dict.sys_yes_no.filter((item) => item.dictValue == value);
      };
    },
  },
  created() {
    this.getAllTag()
    this.getAllSort()
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ["sys_blog_level", "sys_yes_no"],
  data() {
    return {
      defaultProps: { children: "children", label: "label", isLeaf: "leaf" },
      currentId: 0,
      permission: {
        add: ["admin", "blog:add"],
        edit: ["admin", "blog:edit"],
        del: ["admin", "blog:del"],
      },
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        blogSortId: [
          { required: true, message: "分类不能为空", trigger: "blur" },
        ],
        level: [
          { required: true, message: "推荐等级不能为空", trigger: "blur" },
          { pattern: /^[0-9]\d*$/, message: "推荐等级只能为自然数" },
        ],
        openComment: [
          { required: true, message: "网站评论不能为空", trigger: "blur" },
          { pattern: /^[0-9]\d*$/, message: "网站评论只能为自然数" },
        ],
        content: [{ required: true, message: "内容不能为空", trigger: "blur" }],
      },
      show: false,
      size: 2,
      isChange: false,
      formLabelWidth: "120px",
      tagDatas: [],
      fileId:null,
      sorts:[],
      tags:[],
      headers: {
        'Authorization': getToken()
      },
    };
  },
  methods: {
    // 添加取消之后
    [CRUD.HOOK.afterAddCancel]() {
      this.fileId = null;
    },
    // 编辑取消之后
    [CRUD.HOOK.afterEditCancel]() {
      this.fileId = null;
    },
    // 添加取消之前
    [CRUD.HOOK.beforeAddCancel]() {
      if (this.isChange) {
        this.$confirm("是否关闭博客编辑窗口", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            // 清空触发器
            clearInterval(this.interval);
            this.isChange = false;
            this.changeCount = 0;
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消关闭",
            });
          });
      } else {
        // 清空触发器
        clearInterval(this.interval);
        this.isChange = false;
        this.changeCount = 0;
      }
    },
    // 编辑取消之前
    [CRUD.HOOK.beforeEditCancel]() {},
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (this.tagValue.length === 0) {
        this.$message({
          message: "标签不能为空",
          type: "warning",
        });
        return false;
      }
      crud.form.tags = blogTags;
      crud.form.fileId = this.fileId
      return true;
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
       this.getAllTag()
       this.getAllSort()
      if (form.fileId) {
        this.fileId = form.fileId;
      }
    },
    // 新增前将多选的值设置为空
    [CRUD.HOOK.beforeToAdd]() {
      this.tagDatas = [];
      this.fileId = null;
    },
    // 初始化编辑时的标签
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.tagDatas = [];
      blogTags = [];
      const _this = this;
      form.tags.forEach(function (tag, index) {
        _this.tagDatas.push(tag.id);
        const temp_tag = { id: tag.id };
        blogTags.push(temp_tag);
      });
    },
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false;
      }
    },

    toggleShow() {
      this.show = !this.show;
    },
     cropUploadSuccess(jsonData, field) {
      this.fileId = jsonData.data.url;
    },
    // 内容改变，触发监听
    contentChange: function () {
      this.isChange = true;
    },
    changeTag(value) {
      blogTags = [];
      value.forEach(function (data, index) {
        const tag = { id: data };
        blogTags.push(tag);
      });
    },
    deleteTag(value) {
      blogTags.forEach(function (data, index) {
        if (data.id === value) {
          blogTags.splice(index, value);
        }
      });
    },
    getAllSort() {
       getSortByPage({ queryAll: true }).then((res) => {
      if (res.code === this.$ECode.SUCCESS) {
        this.sorts = res.data.data;
      }
    });
    },
    getAllTag() {
       getTagByPage({ queryAll: true }).then((res) => {
      if (res.code === this.$ECode.SUCCESS) {
        this.tags = res.data.data;
      }
    });
    },
     handleFileSuccess(res, file) {
        this.fileId = URL.createObjectURL(file.raw);
      },
    beforeFileUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传标题图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传标题图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
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

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 195px;
    height: 105px;
    line-height: 105px;
    text-align: center;
     border: dashed 1px #c0c0c0;
    
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

.el-dialog__body {
  padding-top: 10px;
  padding-bottom: 0px;
}
.el-dialog {
  min-height: 400px;
}
.el-upload__tip {
  margin-top: 10px;
  margin-left: 10px;
  color: #3e999f;
}
.upload-demo {
  margin-top: 50px;
}
.tipBox {
  margin-bottom: 30px;
}
.tip {
  font-size: 14px;
  font-weight: bold;
  color: #808080;
}
.tipItem {
  line-height: 22px;
  color: #a9a9a9;
}
</style>
