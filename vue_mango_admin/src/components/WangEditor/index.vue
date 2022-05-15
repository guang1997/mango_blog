<template>
    <div style="border: 1px solid #ccc;">
        <Toolbar
            style="border-bottom: 1px solid #ccc"
            :editor="editor"
            :defaultConfig="toolbarConfig"
            :mode="mode"
        />
        <Editor
            style="height: 500px; overflow-y: hidden;"
            v-model="textData"
            :defaultConfig="editorConfig"
            :mode="mode"
            @onCreated="onCreated"
        />
    </div>
</template>
<script>
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

export default Vue.extend({
     props: ["content"],
    components: { Editor, Toolbar },
    data() {
        return {
            editor: null,
            textData: this.content,
            toolbarConfig: { },
            editorConfig: {
                MENU_CONF: {
                    // 暂时使用base64方式插入图片
                    uploadImage: {
                        base64LimitSize: 2 * 1024 * 1024,

                    }
                }
            },
            mode: 'default', // or 'simple'
        }
    },
    methods: {
        onCreated(editor) {
            
            this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
            console.log(editor.getAllMenuKeys())
        },
        //获取data
        getData: function() {
            return this.editor.getHtml();
        },

    },
    beforeDestroy() {
         const editor = this.editor
            if (editor == null) return
            editor.destroy() // 组件销毁时，及时销毁编辑器
    }
})
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
