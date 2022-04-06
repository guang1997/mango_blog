<template>
<div class="app-container">
    <iframe id="myIframe" :src="druidApi" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>
  import {Loading} from "element-ui";
  import { mapGetters } from 'vuex';
  export default {
    computed: {
    ...mapGetters([
      'druidApi'
    ])
  },
    mounted() {
      let iframe = document.getElementById("myIframe")
      let uploadLoading = Loading.service({
        lock: true,
        text: '正在努力加载中……'
      })
      if (iframe.attachEvent) {
        iframe.attachEvent("onload", function () {
          uploadLoading.close();
        });
      } else {
        iframe.onload = function () {
          uploadLoading.close();
        };
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

