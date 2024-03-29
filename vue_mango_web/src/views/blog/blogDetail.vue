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
      <div :class="buttonClass">
        <div class="article-detail__like" v-if="blog.liked === true">
          <el-button type="success" @click.prevent="likeBlog($event)">👍🏻 {{ likeText }}</el-button>
        </div>
        <div class="article-detail__like" v-else-if="blog.liked === false">
          <el-button type="success" plain @click.prevent="likeBlog($event)">👍🏻 {{ likeText }}</el-button>
        </div>
      </div>

      <div class="article-detail__copyright">
        <copyright :url="url" :blogId="blog.id"></copyright>
      </div>
      <div class="article-detail__share">
        <share :tags="blog.tags" :summary="blog.summary" :title="blog.title"></share>
      </div>
      <div class="article-detail__prevnext">
        <prevnext :createTime="blog.createTime"></prevnext>
      </div>
      <div class="article-detail__comment">
        <a id="a_cm"></a>
        <div class="comment__title">
          <i class="el-icon-chat-dot-round"></i>
          <span>文章评论</span>
        </div>
        <div class="comment__submit">
          <submit @submitContent="submitContent" @getComments="getCommentByPage"></submit>
        </div>
        <div class="comment__total">
          <span>{{ total }}条评论</span>
        </div>
        <div class="comment__list">
          <comments :comments="comments" @submitReply="submitReply" @likeComment="likeComment"
            @getCommentByPage="getCommentByPage"></comments>
        </div>
        <div class="comment__page" v-if="total">
          <el-pagination :current-page.sync="page" :total="total" layout="prev, pager, next" :page-size="size"
            @current-change="currentChange"></el-pagination>
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
import { storage } from "@/utils/storage";
import loginApi from "@/api/login";
// import "@/assets/css/quill.snow.css";
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
    };
  },
  // 动态属性
  data() {
    return {
      page: 1,
      size: 10,
      total: 0,
      blog: {},
      comments: [],
      flatTree: null,
      components: [],
      buttonClass: null
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
      return `${process.env.VUE_APP_BASE_API}/blog/${this.blog.id}`;
    },
    likeText() {
      if (this.blog.liked) {
        this.buttonClass = "live2d_liked_blog_button";
        return "已赞";
      } else {
        this.buttonClass = "live2d_like_blog_button";
        return "赞";
      }
    },
  },
  watch: {
    $route(to, from) {
      if (to.params.id !== from.params.id) {
        this.$nextTick(() => {
          this.collectTitles();
        });
      }
    }
  },
  filters: {},
  mounted() {
    window.addEventListener("scroll", this.handleScroll, false);
  },
  created() {
    this.updateVisitorInfo()
    this.asyncData(this.$route.params.id)
  },
  updated() { 
    // 设置目录
    this.collectTitles();
    // 设置额外属性
    this.setAttr()
  },

  // async asyncData({ route, isServer, _ip }) {
  //   this.blogId = route.params.id
  //   const res = await blogApi.getBlogById({
  //     id: route.params.id,
  //     userId: storage.getVisitor() ? storage.getVisitor().id : "",
  //     browserFinger: storage.getMangoBlogBrowserFinger(),
  //     screenInformation: JSON.parse(storage.getMangoBlogScreenInformation()),
  //     page: 1,
  //     size: 10,
  //   });
  //   if (res.code === 20000) {
  //     // if (!isServer) setTimeout(() => jumpAnchor(route), 0);
  //     return {
  //       blog: res.data.data,
  //       comments: res.data.comment,
  //       total: res.data.commentCount,
  //     };
  //   }
  // },
  methods: {
    ...mapMutations(["setCatalogs", "setActiveCatalog", "setVisitor"]),
    async asyncData(blogId) {
      const res = await blogApi.getBlogById({
        id: blogId,
        userId: storage.getVisitor() ? storage.getVisitor().id : "",
        browserFinger: storage.getMangoBlogBrowserFinger(),
        screenInformation: JSON.parse(storage.getMangoBlogScreenInformation()),
        page: 1,
        size: 10,
      });
      if (res.code === 20000) {
        this.blog = res.data.data
        // 解决使用v-html后内容未能解析成html的问题
        this.blog.content = this.blog.content.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&').replace(/&quot;/g, '"').replace(/&apos;/g, "'")
        this.comments = res.data.comment
        this.total = res.data.commentCount
      }
    },
    async likeBlog(event) {
      const isLiked = this.blog.liked ? true : false;
      const res = await commentApi.likeBlog({
        blogId: this.blog.id,
        isLiked,
        likeCount: this.blog.likeCount,
        userId: this.visitorInfo ? this.visitorInfo.id : "",
        source: "BLOG_INFO_LIKES",
        screenInformation: JSON.parse(storage.getMangoBlogScreenInformation()),
        browserFinger: storage.getMangoBlogBrowserFinger(),
        avatar:
          this.visitorInfo && this.visitorInfo.avatar
            ? this.visitorInfo.avatar
            : "",
        nickname:
          this.visitorInfo && this.visitorInfo.nickname
            ? this.visitorInfo.nickname
            : "",
      });
      if (res.code === 20000) {
        this.$message({
          type: "success",
          message: res.data,
        });

        if (isLiked === false) {
          // 一开始没点过赞，后来点了赞
          this.blog.likeCount = this.blog.likeCount + 1;
          this.blog.liked = true;
        } else if (isLiked === true) {
          // 一开始点了赞，后来取消了赞
          this.blog.likeCount =
            this.blog.likeCount <= 0 ? 0 : this.blog.likeCount - 1;
          this.blog.liked = false;
        }
      }
      // 取消标签焦点，加上button的plain样式，否则会一直显示非朴素按钮
      event.target.blur();
      if (event.target.nodeName == "SPAN") {
        event.target.parentNode.blur();
      }
    },
    async likeComment(message) {
      const isLiked = message.liked ? true : false;
      const likeRes = await commentApi.likeComment({
        blogId: this.blog.id,
        isLiked,
        userId: this.visitorInfo.id,
        source: "BLOG_INFO_COMMENT_LIKES",
        parentId: message.id,
        avatar:
          this.visitorInfo && this.visitorInfo.avatar
            ? this.visitorInfo.avatar
            : "",
        nickname:
          this.visitorInfo && this.visitorInfo.nickname
            ? this.visitorInfo.nickname
            : "",
      });
      if (likeRes.code === 20000) {
        this.$message({
          type: "success",
          message: likeRes.data,
        });
        this.getCommentByPage();
      }
    },
    submitContent(content, cb) {
      this.submit(content, null, cb);
    },
    submitReply(content, currentReplyComment, cb) {
      this.submit(content, currentReplyComment, cb);
    },
    async submit(content, currentReplyComment, cb) {
      if (this.visitorInfo.commentStatus == 0) {
        this.$message({
          type: "warning",
          message: "您被禁言了，请联系管理员处理",
        });
        return;
      }
      let parentId = "0";
      let answerNickname;
      if (currentReplyComment) {
        if (currentReplyComment.parentId === "0") {
          parentId = currentReplyComment.id;
        } else {
          parentId = currentReplyComment.parentId;
        }
        answerNickname = currentReplyComment.nickname;
      }
      const res = await commentApi.saveComment({
        blogId: this.$route.params.id,
        nickname: this.visitorInfo.nickname,
        avatar: this.visitorInfo.avatar,
        userId: this.visitorInfo.id,
        content: content,
        parentId,
        source: "BLOG_INFO_MESSAGE",
        answerNickname,
      });
      if (res.code === 20000) {
        if (cb) cb();
        this.$message({
          type: "success",
          message: "评论成功",
        });
        this.getCommentByPage();
      }
    },
    async currentChange(val) {
      this.page = val;
      this.getCommentByPage();
    },
    async getCommentByPage() {
      const commentRes = await commentApi.getCommentByPage({
        page: this.page,
        size: this.size,
        blogId: this.$route.params.id,
        queryLike: true,
        userId: this.visitorInfo.id,
      });
      if (commentRes.code === 20000) {
        this.total = commentRes.data.total;
        this.comments = commentRes.data.data;
      }
    },
    collectTitles() {
      const selectors = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6']
        .map((et) => '.article-detail__body ' + et)
        .join(',')

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
    setAttr() {
      // 设置复选框不能被选中
      var nodeList = document.querySelectorAll(".article-detail__body input");
      if(!nodeList) return;
      for(var i = 0; i < nodeList.length; i++) {
        if(nodeList[i].type=="checkbox") {
          nodeList[i].disabled=true
        }
      }
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
    updateVisitorInfo() {
      // 如果用户没有退出账号就关闭网页了，再次打开网页的时候去更新网页
      if (storage.getVisitor()) {
        loginApi.getUser(storage.getVisitor()).then(res => {
          if (res.code === 20000) {
            this.setVisitorInfo(res.data)
          }
        })
      }
    },
    setVisitorInfo(info) {
      this.setVisitor(info);
      storage.setVisitor(info);
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

      >[class^="el-icon-"] {
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

    pre>code {
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
