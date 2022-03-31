<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: 测试</div>
  </div>
</template>

<script>
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/polar'
import { getServers } from '@/api/monitor/monitor'
export default {
  name: 'ServerMonitor',
  components: {
    'v-chart': ECharts
  },
  data() {
    return {
      path:"ws:/localhost:10001/admin/websocket",
      socket:""
    }
  },
   mounted () {
        // 初始化
        this.init()
    },
  created() {
    //绑定事件
	window.addEventListener('beforeunload', e => this.close(e))
  },
  destroyed() {
     // 销毁监听
       window.removeEventListener('beforeunload', e => this.closeWebsocket(e))
  },
  methods: {
    init: function () {
            if(typeof(WebSocket) === "undefined"){
                alert("您的浏览器不支持socket")
            }else{
                // 实例化socket
                this.socket = new WebSocket(this.path)
                // 监听socket连接
                this.socket.onopen = this.open
                // 监听socket错误信息
                this.socket.onerror = this.error
                // 监听socket消息
                this.socket.onmessage = this.getMessage
            }
        },
        open: function () {
            console.log("socket连接成功")
        },
        error: function () {
            console.log("连接错误")
        },
        getMessage: function (msg) {
            console.log(msg)
        },
        send: function () {
            this.socket.send(params)
        },
        close: function () {
            console.log("socket已经关闭")
        }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .box-card {
    margin-bottom: 5px;
    span {
      margin-right: 28px;
    }
    .el-icon-refresh {
      margin-right: 10px;
      float: right;
      cursor:pointer;
    }
  }
  .cpu, .memory, .swap, .disk  {
    width: 20%;
    float: left;
    padding-bottom: 20px;
    margin-right: 5%;
  }
 .title {
   text-align: center;
   font-size: 15px;
   font-weight: 500;
   color: #999;
   margin-bottom: 16px;
 }
 .footer {
    text-align: center;
    font-size: 15px;
    font-weight: 500;
    color: #999;
    margin-top: -5px;
    margin-bottom: 10px;
  }
  .content {
    text-align: center;
    margin-top: 5px;
    margin-bottom: 5px;
  }
</style>
