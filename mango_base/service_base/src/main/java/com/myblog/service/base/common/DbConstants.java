package com.myblog.service.base.common;

public interface DbConstants {

    interface Base {
        String IS_DELETED = "is_deleted";
        String SORT = "sort";
        String ID = "id";
        String PID = "pid";
        String CREATE_TIME = "create_time";
        String UPDATE_TIME = "update_time";
        String ROLE_ID = "role_id";
        String SUMMARY = "summary";
    }

    interface Admin{
        String USERNAME = "username";
        String PASSWORD = "password";
        String EMAIL = "email";
        String PHONE = "phone";
        String ENABLED = "enabled";
        String NICKNAME = "nickname";
        String GENDER = "gender";
        String LAST_LOGIN_TIME = "last_login_time";
        String QQ_NUMBER = "qq_number";
        String WE_CHAT = "we_chat";
    }

    interface Menu{
        String NAME = "name";
        String COMPONENT = "component";
        String TITLE = "title";

        String PID = "pid";
    }

    interface Role{
        String ROLE_NAME = "role_name";
    }

    interface RoleMenu{
        String MENU_ID = "menu_id";
    }

    interface RoleAdmin{
        String ADMIN_ID = "admin_id";
    }

    interface Tag {
        String TAG_NAME = "tag_name";
    }

    interface BlogTag {
        String TAG_ID = "tag_id";
        String BLOG_ID = "blog_id";
    }

    interface Sort {
        String SORT_NAME = "sort_name";
        String SORT_LEVEL = "sort_level";
    }

    interface Blog {
        String BLOG_SORT_ID = "blog_sort_id";
        String TITLE = "title";
        String LEVEL = "level";
        String OPEN_COMMENT = "open_comment";
    }

	interface Dict {
        String DICT_NAME = "dict_name";
    }

    interface DictDetail {
        String DICT_ID = "dict_id";
        String DICT_LABEL = "dict_label";
    }

    interface Link {
        String TITLE = "title";
        String URL = "url";
        String LINK_STATUS = "link_status";
    }

    interface Comment {
        String CONTENT = "content";
        String SOURCE = "source";
        String TYPE = "type";
        String STATUS = "status";
        String PARENT_ID = "parent_id";
        String BLOG_ID = "blog_id";
        String IP = "ip";
        String USER_ID = "user_id";
        String UNIQUE_KEY = "unique_key";
        String BROWSER_FINGER = "browser_finger";
    }

    interface WebVisit {
        String BEHAVIOR = "behavior";
        String REQUEST_TIME = "request_time";

        String IP = "ip";
    }

    interface EmailConfig {
        String SOURCE = "source";
    }

    interface User {
        String EMAIL = "email";
        String USERNAME = "username";
        String NICKNAME = "nickname";
        String STATUS = "status";
        String COMMENT_STATUS = "comment_status";
    }
}
