<template>
  <div class="editor" id="editor"></div>
</template>
<script>
import E from "wangeditor";
import highlight from 'highlight.js'
import 'highlight.js/styles/monokai-sublime.css'
export default {
  props: ["content"],
  data() {
    return {
         textData: this.content, //初始化内容
    };
  },
  mounted() {
    this.initE();
  },
  methods: {
    initE() {
      this.editor = new E("#editor");
      //菜单栏
      this.editor.config.menus = [
        "head", // 标题
        "bold", // 粗体
        "fontSize", // 字号
        "fontName", // 字体
        "italic", // 斜体
        "underline", // 下划线
        "strikeThrough", // 删除线
        "foreColor", // 文字颜色
        "backColor", // 背景颜色
        "link", // 插入链接
        "list", // 列表
        "justify", // 对齐方式
        "quote", // 引用
        "emoticon", // 表情
        "image", // 插入图片
        "table", // 表格
        "code", // 插入代码
        "undo", // 撤销
        "redo", // 重复
      ];
      //为true，则上传后的图片转为base64编码，为false,则上传图片到服务器，二者不能同时使用
      this.editor.config.uploadImgShowBase64 = true;
      //服务器接收的上传图片的属性名
    //   this.editor.config.uploadFileName = "file";
      //服务器上传图片的接口地址
    //   this.editor.config.uploadImgServer = `http://localhost:3000/upload`;
      // 默认限制图片大小是 5M ，可以自己修改。
    //   this.editor.config.uploadImgMaxSize = 2 * 1024 * 1024; // 2M
      // 限制类型,可自定义配置,默认为： ['jpg', 'jpeg', 'png', 'gif', 'bmp']
    //   this.editor.config.uploadImgAccept = ['jpg', 'jpeg',"png", "gif", "bmp", "webp"];
      //上传图片过程中钩子函数
    //   this.editor.config.uploadImgHooks = {
    //     // 上传图片之前
    //     before: function (xhr) {
    //       // console.log(xhr);

    //       // // 可阻止图片上传
    //       // return {
    //       //   prevent: true,
    //       //   msg: "需要提示给用户的错误信息",
    //       // };
    //     },
    //     // 图片上传并返回了结果，图片插入已成功
    //     success: function (xhr) {
    //       console.log("success", xhr);
    //     },
    //     // 图片上传并返回了结果，但图片插入时出错了
    //     fail: function (xhr, editor, resData) {
    //       console.log("fail", resData);
    //     },
    //     // 上传图片出错，一般为 http 请求的错误
    //     error: function (xhr, editor, resData) {
    //       console.log("error", xhr, resData);
    //     },
    //     // 上传图片超时
    //     timeout: function (xhr) {
    //       console.log("timeout");
    //     },
    //   };
    // 挂载highlight插件
    this.editor.highlight = highlight
    // 在输入内容时，把内容传给父组件
      this.editor.config.onchange = (html) => {
        this.$emit("contentChange", html); // 将内容同步到父组件中
      };
      this.editor.create();
      //初始化富文本编辑器时显示的内容
      this.editor.txt.html(this.textData);
    },
  },
};

</script>
