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
        String MOBILE = "mobile";
        String ENABLED = "enabled";
        String NICKNAME = "nickname";
        String GENDER = "gender";
        String LAST_LOGIN_TIME = "last_login_time";
    }

    interface Menu{
        String NAME = "name";
        String COMPONENT = "component";
        String TITLE = "title";
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
    }

    interface Sort {
        String SORT_NAME = "sort_name";
        String SORT_LEVEL = "sort_level";
    }

    interface Blog {
        String BLOG_SORT_ID = "blog_sort_id";
    }
	interface Dict {
        String DICT_NAME = "dict_name";
    }
}
