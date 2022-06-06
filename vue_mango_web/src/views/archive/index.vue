<template>
  <div class="archives">
    <layout _title="归档" cover="/static/img/cover/archive.jpeg">
      <template v-if="archives.length">
        <div class="archives__year" v-for="(range, index) in archives" :key="index">
          <div class="year-text">{{ range.month || range.year }}</div>
          <el-timeline>
            <el-timeline-item
              v-for="(blog, mi) in range.childrens"
              :key="'art_' + mi"
              type="primary"
              :hide-timestamp="true"
            >
              <div class="archives__content">
                <div class="content-left">
                  <router-link :to="'/app/blog/' + blog.id">
                    <img v-lazy="blog.fileId" alt="" />
                  </router-link>
                </div>
                <div class="content-right">
                  <div class="content-right__title">
                    <router-link :to="'/app/blog/' + blog.id">
                      {{ blog.title }}
                    </router-link>
                  </div>
                  <div class="content-right__date">
                    <i class="el-icon-date"></i>
                    {{ blog.createTime }}
                  </div>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </template>
      <empty v-else></empty>

      <div class="archives__page" v-if="archives.length">
        <el-pagination
          :total="total"
          layout="prev, pager, next"
          :page-size="size"
          @current-change="currentChange"
        ></el-pagination>
      </div>
    </layout>
  </div>
</template>
<script>
import archiveApi from '@/api/archive'

async function getArchiveRes(route, page = 1, size = 10) {

  const params = {
    page,
    size
  }
  if (route.query.filter && /(\d+)-(\d+)/.test(route.query.filter)) {
    params.queryByMonth = true
    params.month = route.query.filter
  }
  const archiveRes = await archiveApi.getArchives(params)
  return archiveRes
}

export default {
  name: 'archives',
  metaInfo() {
    return {
      title: `归档  - Lisite's Blog`
    }
  },
  data() {
    return {
      page: 1,
      size: 10,
      total: 0,
      archives: []
    }
  },
  async asyncData({ route }) {
    const archiveRes = await getArchiveRes(route)
    if (archiveRes.code === 20000) return { archives: archiveRes.data.data, total: archiveRes.data.total }
  },
  watch: {
    $route() {
      this.getArchives()
    }
  },
  methods: {
    currentChange(val) {
      this.page = val
      this.getArchives()
    },
    async getArchives() {
      const archiveRes = await getArchiveRes(this.$route, this.page, this.size)
      if (archiveRes.code === 20000) {
        this.archives = archiveRes.data.data
        this.total = archiveRes.data.total
      }
    }
  }
}
</script>
<style lang="scss">
@import '~@/style/index.scss';
.archives {
  &__year {
    padding: 12px;
    .year-text {
      font-size: 22px;
      padding: 16px 0 28px 0;
    }
  }
  &__content {
    display: flex;
    align-items: center;
    .content-left {
      width: 80px;
      height: 80px;
      flex: 0 0 auto;
      overflow: hidden;
      a {
        display: inline-block;
        width: 80px;
        height: 80px;
      }
      img {
        border-radius: 4px;
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      img[src=''],
      img:not([src]) {
        opacity: 0;
      }
    }
    .content-right {
      margin-left: 24px;
      &__title,
      &__title a {
        transition: all 0.38s ease;
        @include clamp(2);
      }
      &__title {
        font-size: 16px;
        padding: 8px 0;
      }
      &__title:hover a,
      &__title:hover {
        @include themeify() {
          color: themed('color-ele-primary');
        }
        transform: translateX(12px);
      }
      &__date {
        @include themeify() {
          color: themed('color-home-article-detail');
        }
        padding: 8px 0;
      }
    }
  }
  &__page {
    padding: 16px;
    @include flex-box-center;
  }
}
</style>
