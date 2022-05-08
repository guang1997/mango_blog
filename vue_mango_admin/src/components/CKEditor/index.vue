<template>
  <div class="app-container">
    <ckeditor id="editor" :config="editorConfig"></ckeditor>
  </div>
</template>

<script>

export default {
  props: ["content", "height"],
  mounted() {
    // let that = this;
    // 配置ckeditor插件
    // console.log(window.CKEDITOR)
    // CKEDITOR.plugins.addExternal( 'codesnippet', '/public/ckeditor/plugins/codesnippet/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'panelbutton', '/public/ckeditor/plugins/panelbutton/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'floatpanel', '/public/ckeditor/plugins/floatpanel/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'colorbutton', '/public/ckeditor/plugins/colorbutton/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'markdown', '/public/ckeditor/plugins/markdown/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'colordialog', '/public/ckeditor/plugins/colordialog/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'dialog', '/public/ckeditor/plugins/dialog/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'dialogui', '/public/ckeditor/plugins/dialogui/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'smiley', '/public/ckeditor/plugins/smiley/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'widget', '/public/ckeditor/plugins/widget/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'lineutils', '/public/ckeditor/plugins/lineutils/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'eqneditor', '/public/ckeditor/plugins/eqneditor/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'justify', '/public/ckeditor/plugins/justify/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'tableresize', '/public/ckeditor/plugins/tableresize/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'wordcount', '/public/ckeditor/plugins/wordcount/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'notification', '/public/ckeditor/plugins/notification/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'video', '/public/ckeditor/plugins/video/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'fakeobjects', '/public/ckeditor/plugins/fakeobjects/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'liststyle', '/public/ckeditor/plugins/liststyle/', 'plugin.js' );
    // CKEDITOR.plugins.addExternal( 'pasteUploadImage', '/static/ckeditor/plugins/pasteUploadImage/', 'plugin.js' );

    //使用ckeditor替换textarea，设置代码块风格为 zenburn
    // 上传时，携带token信息，以便于被feign拦截后传递给mogu-admin获取七牛云相关配置
    // CKEDITOR.replace('editor',
    //   {height: this.height,
    //     width: '100%',
    //     toolbar: 'toolbar_Full',
    //     codeSnippet_theme: 'zenburn',
    //     customConfig: '/static/ckeditor/config.js',
    //     extraPlugins: 'codesnippet,panelbutton,floatpanel,colorbutton,markdown,colordialog,dialog,dialogui,smiley,widget,lineutils,eqneditor,justify,tableresize,wordcount,notification,video,fakeobjects,liststyle,pasteUploadImage'
    //   });

    // this.editor = CKEDITOR.instances.editor;
    this.editor.setData(this.content); //初始化内容

 // 在输入内容时，把内容传给父组件
      this.editor.config.onchange = (html) => {
        this.$emit("contentChange", html); // 将内容同步到父组件中
      };
    // 一秒钟通知子组件，ckeditor中内容改变
    // that.editor.on('change', function( event ) {
    //   that.timeout = setTimeout(function() {
    //     that.fun();
    //   }, 1000);
    // });

  },
  created() {
    this.textData = this.content;
  },
  watch: {
    content: function() {
      this.textData = this.content;
    }
  },
  data() {
    return {
      editor: null, //编辑器对象
      textData: this.content, //初始化内容
      editorConfig: { // 编辑器配置

      }
    }
  },
  methods: {
    //获取data
    getData: function() {
      return this.editor.getData();
    },
    setData: function(data) {
      return this.editor.setData(data);
    },
    initData: function() {
      try {
        this.editor.setData("");
      } catch (error) {
        console.log("CKEditor还未加载");
      }
    },
    fun: function() {
      this.$emit("contentChange", "");
    }
  }
}

</script>
