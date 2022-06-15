package com.myblog.service.base.common;

public interface Constants {

    interface Symbol {
        String COMMA1 = ",";
        String COMMA2 = "-";
        String COMMA3 = ".";
        String COMMA4 = "/";
        String COMMA5 = ";";
    }

    interface ReplyField {
        String ID = "id";
        String TOKEN = "token";
        String ROLES = "roles";
        String AVATAR = "avatar";
        String USERNAME = "username";
        String NICKNAME = "nickname";
        String PHONE = "phone";
        String EMAIL = "email";
        String QQ_NUMBER = "qqNumber";
        String WE_CHAT = "weChat";
        String GENDER = "gender";
        String CREATE_TIME = "createTime";
        String EXPIRES_SECOND = "expiresSecond";
        String SECRET_KEY = "secretKey";
        String HEADER = "Authorization";
        String MENU = "menu";
        String DEFAULT_VALUE = "defaultValue";
        String LIST = "list";
        String DATA = "data";
        String PAGE = "page";
        String SIZE = "size";
        String TOTAL = "total";
        String URL = "url";
        String SYS = "sys";
        String CPU = "cpu";
        String MEMORY = "memory";
        String SWAP = "swap";
        String DISK = "disk";
        String TIME = "time";
        String CODE = "code";
        String COMMENT_COUNT = "commentCount";
        String BLOG_COUNT = "blogCount";
        String USER_COUNT = "userCount";
        String VISIT_COUNT = "visitCount";
        String NAME = "name";
        String CONTRIBUTE_DATE = "contributeDate";
        String BLOG_CONTRIBUTE_COUNT = "blogContributeCount";
        String RADAR_INDICATOR = "radarIndicator";
        String RADAR_CHART_DATA = "radarChartData";
        String DATE = "date";
        String VISIT_LIST = "visitList";
        String BLOG_TAGS = "blogTags";
        String BLOG_SORTS = "blogSorts";
        String MENU_BUTTONS = "menuButtons";
        String PREV_BLOG = "prevBlog";
        String NEXT_BLOG = "nextBlog";
        String OS = "os";
        String BROWSER = "browser";
        String COMMENT = "comment";
    }

    interface SystemConstant {
        String WIN = "win";
    }

    /**
     * 评论状态: 0-待审核, 1-审核通过, 2-审核不通过
     */
    interface CommentStatus {
        int PENDING_REVIEW = 0;
        int REVIEWED = 1;
        int REJECT = 2;
    }

    /**
     * 评论类型: 0-评论，1-点赞
     */
    interface CommentType {
        int MESSAGE = 0;
        int LIKES = 1;
    }

    /**
     * 邮箱来源
     */
    interface EmailSource {
        String WEB = "WEB";
        String ADMIN = "ADMIN";
    }

    interface EmailParam {
        String WEB_LINK = "webLink";
        String ADMIN_LINK = "adminLink";
    }
    /**
     * 用户标签
     */
    interface UserTag {
        int ORDINARY_USERS = 0;
        int ADMIN = 1;
        int BLOG_USERS = 2;
    }

    /**
     * 通用状态
     */
    interface CommonStatus {
        int DISABLED = 0;
        int ENABLED = 1;
    }
}
