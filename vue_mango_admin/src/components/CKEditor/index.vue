<template>
  <div class="app-container">
    <ckeditor
      id="editor"
      v-model="textData"
      :config="editorConfig"
      :editor-url="editorUrl"
      rows="10"
      cols="60"
    ></ckeditor>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
export default {
  props: ["content"],
  name: "app",
  data() {
    return {
      textData: this.content,
      editorConfig: {
        extraPlugins: "quicktable,tableresizerowandcolumn,emoji,codesnippet,editorplaceholder,autogrow",// 启动插件
        toolbarGroups: [
          { name: "document", groups: ["mode", "document", "doctools"] },
          { name: "clipboard", groups: ["clipboard", "undo"] },
          {
            name: "editing",
            groups: ["find", "selection", "spellchecker", "editing"],
          },
          { name: "forms", groups: ["forms"] },
          "/",
          { name: "basicstyles", groups: ["basicstyles", "cleanup"] },
          {
            name: "paragraph",
            groups: ["list", "indent", "blocks", "align", "bidi", "paragraph"],
          },
          { name: "links", groups: ["links"] },
          { name: "insert", groups: ["insert"] },
          "/",
          { name: "styles", groups: ["styles"] },
          { name: "colors", groups: ["colors"] },
          { name: "tools", groups: ["tools"] },
          { name: "others", groups: ["others"] },
          { name: "about", groups: ["about"] },
          {name: 'codesnippet'}// 代码片段
        ],
        codeSnippet_theme: 'vs2015',// 代码高亮主题
        editorplaceholder: '开始写文章吧~~~',// 编辑器占位符
        autoGrow_minHeight: 800,// 自动增长最小高度
        removeButtons:
          "Save,Print,NewPage,About,Smiley,BidiRtl,BidiLtr,Language,CreateDiv,Iframe",// 移除按钮
      },
      editorUrl: "../../../public/ckeditor/ckeditor.js",
    };
  },
  created() {
    this.textData = this.content;
  },
  watch: {
    content: function () {
      this.textData = this.content;
    },
  },
  methods: {
    //获取data
    getData: function () {
      return this.textData;
    },
  },
};
</script>
