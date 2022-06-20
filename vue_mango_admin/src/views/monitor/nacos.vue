<template>
<div class="app-container">
    <router-link to="http://localhost:8848/nacos" target="_blank"></router-link>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex';
  export default {
    computed: {
    ...mapGetters([
      'nacosApi'
    ])
  },
    mounted() {
       window.open("http://localhost:8848/nacos", "_blank");
    },
    methods: {

    }
  }
</script>

