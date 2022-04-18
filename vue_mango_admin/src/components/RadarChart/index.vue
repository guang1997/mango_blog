<template>
  <div :class="className" :style="{ height: height, width: width }"></div>
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import { debounce } from "@/utils";

export default {
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "400px",
    },
    value: {
      type: Array,
      default: [],
    },
    tagName: {
      type: Array,
      default: [],
    },
    indicator: {
      type: Array,
      default: [],
    },
  },

  data() {
    return {
      chart: null,
    };
  },

  mounted() {
    this.initChart();
    this.__resizeHanlder = debounce(() => {
      if (this.chart) {
        this.chart.resize();
      }
    }, 100);
    window.addEventListener("resize", this.__resizeHanlder);
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    window.removeEventListener("resize", this.__resizeHanlder);
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    initChart() {
      var that = this;
      that.chart = echarts.init(this.$el, "macarons");

      that.chart.on("click", function (param) {
        that.$emit("clickPie", param.dataIndex);
      });
      that.chart.setOption({
        tooltip: {
          trigger: "item",
        },
        legend: {
          type: "scroll",
          bottom: 10,
          data: (function () {
            
            return that.tagName;
          })(),
        },
        visualMap: {
          top: "middle",
          right: 10,
          color: ["purple", "pink"],
          calculable: true,
        },
        radar: {
          indicator: that.indicator,
        },
        series: (function () {
          var series = [];
          for (var i = 1; i <= that.value.length; i++) {
            series.push({
              type: "radar",
              symbol: "none",
              lineStyle: {
                width: 1,
              },
              emphasis: {
                areaStyle: {
                  color: "rgba(0,250,0,0.3)",
                },
              },
              data: that.value,
            });
          }
          return series;
        })(),
      });
    },
  },
};
</script>
