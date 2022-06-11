<template>
  <div class="pannel-articles">
    <el-card>
      <div class="pannel__item-content">
        <div class="pannel__item-title">
          <i class="el-icon-document"></i>
          <span>最新文章</span>
        </div>
        <div class="pannel__item-body">
          <template v-if="newBlogs.length">
            <div class="body-item" v-for="(blog, index) in newBlogs" :key="index">
              <div class="body-pic">
                <router-link :to="'/app/blog/' + blog.id">
                  <img v-lazy="blog.fileId" alt="" />
                </router-link>
              </div>
              <div class="body-info">
                <div class="body-info__title">
                  <router-link class="live2d_router_link" :to="'/app/blog/' + blog.id">
                    {{ blog.title }}
                  </router-link>
                </div>
                <div class="body-info__date">{{ blog.createTime }}</div>
              </div>
            </div>
          </template>
          <empty v-else></empty>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import { mapState } from 'vuex'

export default {
  name: 'pannelBlogs',
  props: {},
  data() {
    return {}
  },
  computed: {
    ...mapState(['newBlogs'])
  },
  methods: {}
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
@import '~@/views/pannel/style/mixins';

.pannel-articles {
  @include articles-comments;
}
</style>
