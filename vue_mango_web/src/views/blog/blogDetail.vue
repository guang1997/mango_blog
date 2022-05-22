<template>
  <div class="article-detail">
    <layout :cover="blog.fileId">
      <div class="article-detail__header" slot="custom-header">
        <h1 class="article-detail__title">{{ blog.title }}</h1>
        <div class="article-detail__info info-1">
          <span>
            <i class="el-icon-date"></i>
            发表时间 {{ blog.createTime }}
          </span>
          <span>&nbsp;|&nbsp;</span>
          <span>
            <i class="el-icon-price-tag"></i>
            标签 {{ tagNames }}
          </span>
        </div>
        <div class="article-detail__info info-2">
          <span>
            <i class="el-icon-chat-dot-round"></i>
            阅读量 {{ blog.clickCount }}
          </span>
          <span>&nbsp;|&nbsp;</span>
          <span>
            <i class="el-icon-chat-dot-round"></i>
            评论数 {{ blog.comments ? blog.comments.length : 0 }}
          </span>
          <span>&nbsp;|&nbsp;</span>
          <span>
            <i class="el-icon-star-off"></i>
            点赞 {{ blog.likeCount }}
          </span>
        </div>
      </div>
      <note>
        <p>{{ blog.summary }}</p>
      </note>
      <div v-html="blog.content" class="article-detail__body ql-editor"></div>
      <div class="article-detail__update">
        <span>最后编辑于：{{ blog.updateTime }}</span>
      </div>
      <div class="article-detail__like">
        <el-button type="primary" :plain="blog.liked" @click="likeBlog"
          >👍🏻 {{ likeText }}</el-button
        >
      </div>
      <div class="article-detail__copyright">
        <copyright :url="url" :blogId="blog.id"></copyright>
      </div>
      <!-- <div class="article-detail__share">
        <share :tags="blog.tags" :abstract="blog.summary" :title="blog.title"></share>
      </div> -->
      <!-- <div class="article-detail__prevnext">
        <prevnext :article="blog"></prevnext>
      </div> -->
      <div class="article-detail__comment">
        <a id="a_cm"></a>
        <div class="comment__title">
          <i class="el-icon-chat-dot-round"></i>
          <span>文章评论</span>
        </div>
        <div class="comment__submit">
          <!-- <submit @submitContent="submitContent"></submit> -->
        </div>
        <div class="comment__total">
          <span>{{ total }}条评论</span>
        </div>
        <!-- <div class="comment__list">
          <comments :comments="comments" @submitReply="submitReply" @addLike="addLike"></comments>
        </div> -->
        <div class="comment__page" v-if="total">
          <el-pagination
            :current-page.sync="currentPage"
            :total="total"
            layout="prev, pager, next"
            :page-size="pageSize"
            @current-change="currentChange"
          ></el-pagination>
        </div>
      </div>
    </layout>
  </div>
</template>
<script>
import { mapState, mapMutations } from "vuex";
import blogApi from "@/api/blog";
import commentApi from "@/api/comment";
import note from "@/components/note/";
import { generateTree } from "@/utils/generateTree";
import { getRandomCharacter } from "@/utils/getRandomCharacter";
import comments from "@/views/components/comments";
import submit from "@/views/components/submit";
import copyright from "./components/copyright";
import share from "./components/share";
import prevnext from "./components/prevnext";
import Fingerprint2 from "fingerprintjs2";
import "@/assets/css/quill.snow.css";
function jumpAnchor(route) {
  if (route.query.anchor === "a_cm") {
    const el = document.querySelector("#a_cm");
    el.scrollIntoView();
  }
}

export default {
  name: "blogDetail",
  components: { note, comments, submit, copyright, share, prevnext },
  props: {},
  metaInfo() {
    return {
      title: `Lisite's Blog - ${this.blog.title}`,
      // meta: [
      //   {
      //     name: 'description',
      //     content: this.blog.summary + ` - Lisite's Blog mapblog`
      //   },
      //   {
      //     name: 'keywords',
      //     content: this.blog.tags.join(',')
      //   }
      // ]
    };
  },
  // 动态属性
  data() {
    return {
      currentPage: 1,
      pageSize: 10,
      total: 0,
      blog: {},
      comments: [],
      flatTree: null,
      components: [],
      userId: ''
    };
  },
  computed: {
    ...mapState(["visitorInfo"]),
    tagNames() {
      if (this.blog.tags) {
        var tagNames = [];
        for (var a = 0; a < this.blog.tags.length; a++) {
          tagNames.push(this.blog.tags[a].tagName);
        }
        return tagNames.join(" ");
      }
      return "";
    },
    url() {
      return `${process.env.VUE_APP_BASE_API}/app/blog/${this.blog.id}`;
    },
    likeText() {
      if (this.blog.liked) return "已赞";
      return "赞";
    },
  },
  watch: {
    $route(to, from) {
      if (to.params.id !== from.params.id) {
        this.$nextTick(() => {
          this.collectTitles();
        });
      }
    },
  },
  filters: {},
  mounted() {
    this.$nextTick(function () {
      Prism.highlightAll();
    });
    this.collectTitles();
    window.addEventListener("scroll", this.handleScroll, false);
  },
  created() {
    setTimeout(() => {
      this.createFingerprint();
    }, 1000);
  },
  updated() {},
  async asyncData({ route, isServer, _ip }) {
    const res = await blogApi.getBlogById({
      id: route.params.id,
    });
    // const commentRes = await commentApi.getBlogComments({
    //   page: 1,
    //   limit: 10,
    //   articleId: route.params.id,
    //   _ip
    // })
    if (res.code === 20000) {
      if (!isServer) setTimeout(() => jumpAnchor(route), 0);
      return {
        blog: res.data.data,
        comments: res.data.data.comments,
        total: res.total,
      };
    }
  },
  methods: {
    ...mapMutations(["setCatalogs", "setActiveCatalog"]),
    async likeBlog() {
      const isLiked = this.blog.liked ? true : false;
      const res = await commentApi.likeBlog({
        blogId: this.blog.id,
        isLiked,
        likeCount: this.blog.likeCount,
        userId: this.userId
      });
      if (res.code === 20000) {
        this.$message({
          type: "success",
          message: res.message,
        });

        if (isLiked === false) {
          // 一开始没点过赞，后来点了赞
          this.blog.likeCount = this.blog.likeCount + 1;
          this.blog.liked = true;
        } else if (isLiked === true) {
          // 一开始点了赞，后来取消了赞
          this.blog.likeCount = this.blog.likeCount - 1;
          this.blog.liked = false;
        }
      } else {
        this.$message({
          type: "error",
          message: res.message,
        });
      }
    },
    async addLike(message) {
      const inc = message.liked ? -1 : 1;
      const likeRes = await commentApi.likeBlogComment({
        id: message._id,
        inc,
      });
      if (likeRes.status === 200) {
        let finder;
        this.comments.some((msg) => {
          if (msg._id === likeRes.data._id) {
            finder = msg;
            return true;
          }
          if (msg.reply && msg.reply.length) {
            let done = false;
            msg.reply.some((er) => {
              if (er._id === likeRes.data._id) {
                finder = er;
                done = true;
              }
            });
            return done;
          }
        });
        if (finder) {
          finder.like = likeRes.data.like;
          finder.liked = likeRes.data.liked;
        }
        this.$message({
          type: "success",
          message: likeRes.info,
        });
      }
    },
    submitContent(content, cb) {
      this.submit(content, null, cb);
    },
    submitReply(content, currentReplyComment, cb) {
      this.submit(content, currentReplyComment, cb);
    },
    async submit(content, currentReplyComment, cb) {
      let parentId;
      let aite;
      if (currentReplyComment) {
        if (currentReplyComment.parentId)
          parentId = currentReplyComment.parentId;
        else parentId = currentReplyComment._id;
        aite = currentReplyComment.name;
      }
      const res = await blogApi.saveArticleComment({
        articleId: this.$route.params.id,
        name: this.visitorInfo.name,
        imgUrl: this.visitorInfo.imgUrl,
        email: this.visitorInfo.email,
        link: this.visitorInfo.link,
        content: content,
        parentId,
        aite,
      });
      if (res.status === 200) {
        if (cb) cb();
        this.$message({
          type: "success",
          message: "评论成功",
        });
        // this.getArticleComments()
      }
    },
    async currentChange(val) {
      this.currentPage = val;
      this.getArticleComments();
    },
    // async getArticleComments() {
    //   const commentRes = await blogApi.getArticleComments({
    //     page: this.currentPage,
    //     limit: this.limit,
    //     articleId: this.$route.params.id
    //   })
    //   if (commentRes.status === 200) {
    //     this.total = commentRes.total
    //     this.comments = commentRes.data
    //   }
    // },
    collectTitles() {
      const selectors = ["h1", "h2", "h3", "h4", "h5", "h6"]
        .map((et) => ".article-detail__body " + et)
        .join(",");
      const nodeList = document.querySelectorAll(selectors);
      if (!nodeList) return;

      const flatTree = Array.from(nodeList).map((node) => {
        const a = document.createElement("a");
        const tempId = getRandomCharacter(4);
        const firstChild = node.firstChild;
        a.setAttribute("name", node.innerText);
        a.setAttribute("id", tempId);
        // node.appendChild(a)
        node.insertBefore(a, firstChild);
        return {
          level: parseInt(node.nodeName.substr(1)),
          name: node.innerText,
          tempId,
        };
      });
      this.flatTree = [...flatTree];
      this.flatTree.reverse();
      const catalogs = generateTree(flatTree);
      this.addTreeLevel(catalogs);
      this.setCatalogs(catalogs);
    },
    addTreeLevel(catalogs, level, order) {
      catalogs.forEach((catalog, index) => {
        if (!level) level = 0;
        catalog.level_tree = level;
        catalog.order = order ? order + "." + (index + 1) : index + 1;
        const dom = document.getElementById(catalog.tempId);
        dom.removeAttribute("name");
        dom.setAttribute("name", catalog.order);
        if (catalog.children && catalog.children.length) {
          this.addTreeLevel(catalog.children, level + 1, catalog.order);
        }
      });
    },

    handleScroll() {
      if (!this.flatTree) return;
      this.flatTree.some((item) => {
        const node = document.getElementById(item.tempId);
        if (node.getBoundingClientRect().y < 5) {
          this.setActiveCatalog(item.tempId);
          return true;
        }
      });
    },
    createFingerprint() {
      Fingerprint2.get((components) => {
        // 参数只有回调函数时，默认浏览器指纹依据全部配置信息进行生成
        const values = components.map(function (component, index) {
          if (index === 0) {
            //把微信浏览器⾥UA的wifi或4G等⽹络替换成空,不然切换⽹络会ID不⼀样
            return component.value.replace(/\bNetType\/\w+\b/, "");
          }
          return component.value;
        });
        this.userId = Fingerprint2.x64hash128(values.join(""), 31); // 生成浏览器指纹
        console.log("userId", this.userId)
        // localStorage.setItem('browserId', murmur); // 存储浏览器指纹，在项目中用于校验用户身份和埋点
      });
    },
  },
  destroyed() {
    window.removeEventListener("scroll", this.handleScroll, false);
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
@import "~@/style/index.scss";
.article-detail {
  &__header {
    width: 100%;
    height: 100%;
    @include flex-box-center;
    flex-direction: column;
  }
  &__body {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Helvetica Neue",
      Lato, Roboto, "PingFang SC", "Microsoft JhengHei", "Microsoft YaHei",
      sans-serif;
    line-height: 2;
    color: #4c4948;
    img {
      width: 100%;
      height: 100%;
    }
  }
  &__title {
    line-height: 1.5;
    padding: 0 12px;
    text-align: center;
    @include themeify() {
      color: themed("color-title");
    }
    @include respond-to(xs) {
      font-size: 18px;
    }
  }

  &__info {
    padding: 0 12px;
    @include themeify() {
      color: themed("color-navbar");
    }
  }
  .info-2 {
    margin-top: 8px;
  }
  &__update {
    margin-top: 20px;
    padding: 14px;
    text-align: right;
    @include themeify() {
      color: themed("color-ele-holder");
    }
  }
  &__like {
    margin-top: 20px;
    padding: 14px;
    text-align: center;
  }
  &__copyright {
    margin-top: 28px;
  }
  &__share {
    margin-top: 12px;
  }
  &__prevnext {
    margin-top: 28px;
  }
  &__comment {
    margin-top: 32px;
    .comment__title {
      padding: 16px 0;
      font-size: 20px;
      font-weight: 700;
      > [class^="el-icon-"] {
        font-weight: 700;
      }
      span {
        margin-left: 12px;
      }
    }
    .comment__total {
      color: #4c4948;
      font-size: 25px;
      font-weight: bold;
      margin-top: 28px;
    }
    .comment__list {
      margin-top: 28px;
    }
    .comment__page {
      @include flex-box-center;
      padding: 16px 0;
    }
  }
  // 覆盖 quill.js 中的部分css
  .ql-editor {
    padding: 0;
    line-height: 2;
    .code-toolbar {
      margin-top: 12px;
    }
    a {
      color: #409eff;
    }
    a:hover {
      text-decoration: underline;
    }
    ul,
    ol {
      padding-left: 0;
    }
    li.ql-indent-1:not(.ql-direction-rtl) {
      padding-left: 3.5em;
    }
    pre > code {
      background: 0 0 !important;
    }
    code:not([class*="language-"]) {
      background-color: #f0f0f0;
      border-radius: 3px;
      font-size: 90%;
      padding: 3px 5px;
    }
    blockquote {
      border-left: 4px solid #ccc;
      margin-bottom: 5px;
      margin-top: 5px;
      padding-left: 16px;
    }
  }
}
</style>
