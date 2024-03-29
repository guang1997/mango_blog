<template>
  <div id="app">
    <router-view v-if="isRouterActive"/>
     <back-top></back-top>
     <live2d></live2d>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import blogApi from "@/api/blog";
import archiveApi from "@/api/archive";
import tagApi from "@/api/tag";
import sortApi from "@/api/sort";
import commentApi from "@/api/comment";
import webConfigApi from "@/api/webConfig";
import Fingerprint2 from "fingerprintjs2";
import { storage } from "@/utils/storage";
import backTop from '@/components/backTop'
import live2d from '@/components/live2d'
export default {
  name: "App",
  provide() {
      return {
        reload: this.reload
      }
  },
  data() {
    return {
      location: [],
      timer: "",
      showBackTop: true,
      isRouterActive: true
    };
  },
   components: { backTop, live2d },
  mounted() {
    this.initPannel()
    this.initWebConfig()
    if (!storage.getMangoBlogBrowserFinger()) {
      setTimeout(() => {
        this.createFingerprint();
      }, 1000);
    }
    if (!storage.getMangoBlogScreenInformation()) {
      this.createScreenInformation();
    }
  },
  methods: {
    ...mapMutations([
      "setArchives",
      "setSorts",
      "setTags",
      "setNewComments",
      "setNewBlogs",
      "setTotals",
      "setBrowserFinger",
      "setScreenInformation",
      "setWebConfig"
    ]),
    initPannel() {
      archiveApi.initArchives({ queryByMonth: true }).then((res) => {
        if (res.code === 20000) this.setArchives(res.data)
      })
      sortApi
        .getSortByPage({
          queryLatest:true
        })
        .then((res) => {
          if (res.code === 20000) {
            this.setTotals({ key: "sort", value: res.data.total });
            this.setSorts(res.data.data);
          }
        });
      tagApi
        .getTagByPage({
          queryLatest:true
        })
        .then((res) => {
          if (res.code === 20000) {
            this.setTotals({ key: "tag", value: res.data.total });
            this.setTags(res.data.data);
          }
        });
      commentApi
        .getCommentByPage({
          page: 1,
          size: 5,
        })
        .then((res) => {
          if (res.code === 20000) this.setNewComments(res.data.data);
        });
      blogApi
        .getBlogByPage({
          page: 1,
          size: 5,
        })
        .then((res) => {
          if (res.code === 20000) {
            this.setTotals({ key: "blog", value: res.data.total });
            this.setNewBlogs(res.data.data);
            
          }
        });
    },
    initWebConfig() {
      webConfigApi.getWebConfig().then((res) => {
        if(res.code === 20000) {
          this.setWebConfig(res.data)
        }
      })
    },
    createFingerprint() {
      let options = (Fingerprint2.Options = {
        excludes: {
          language: true,
          colorDepth: true,
          deviceMemory: true,
          pixelRatio: true,
          availableScreenResolution: true,
          timezoneOffset: true,
          timezone: true,
          sessionStorage: true,
          localStorage: true,
          indexedDb: true,
          addBehavior: true,
          openDatabase: true,
          cpuClass: true,
          doNotTrack: true,
          plugins: true,
          canvas: true,
          webglVendorAndRenderer: true,
          adBlock: true,
          hasLiedLanguages: true,
          hasLiedResolution: true,
          hasLiedOs: true,
          hasLiedBrowser: true,
          touchSupport: true,
          audio: false,
          enumerateDevices: true,
          hardwareConcurrency: true,
        },
      });
      Fingerprint2.get(options, (components) => {
        // 参数只有回调函数时，默认浏览器指纹依据全部配置信息进行生成
        const values = components.map(function (component, index) {
          if (index === 0) {
            //把微信浏览器⾥UA的wifi或4G等⽹络替换成空,不然切换⽹络会ID不⼀样
            return component.value.replace(/\bNetType\/\w+\b/, "");
          }
          return component.value;
        });
        var mangoBlogBrowserFinger = Fingerprint2.x64hash128(
          values.join(""),
          31
        );
        // 保存到localStorage，防止随时变化
        storage.setMangoBlogBrowserFinger(mangoBlogBrowserFinger)
      });
    },
    createScreenInformation() {
      // const div = document.createElement("div");
      // div.style.cssText =
      //   "height: 1in; left: -100%; position: absolute; top: -100%; width: 1in;";
      // document.body.appendChild(div);
      // const devicePixelRatio = window.devicePixelRatio || 1,
      //   dpi = div.offsetWidth * devicePixelRatio;

      const screenInformation = {
        height: window.screen.height,
        width: window.screen.width,
        pixelDepth: window.screen.pixelDepth,
      };
      // 保存到localStorage，防止随时变化
      storage.setMangoBlogScreenInformation(JSON.stringify(screenInformation));
    },
    // 使用provide/inject解决多个组件在同一页面无法跳转的问题
    reload() {
      this.isRouterActive = false
      this.$nextTick(function() {
        this.isRouterActive = true
      });
    }
  },
};
</script>

<style lang="scss">
@import "~@/style/reset.scss";
// @import "./assets/css/prism.css";
@import "./assets/css/emoji-sprite.css";
</style>
