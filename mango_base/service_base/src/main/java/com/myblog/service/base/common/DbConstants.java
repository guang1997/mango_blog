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
    }

    interface Admin{
        String USERNAME = "username";
        String PASSWORD = "password";
        String EMAIL = "email";
        String MOBILE = "mobile";
        String STATUS = "status";
        String NICKNAME = "nickname";
        String GENDER = "gender";
    }

    interface Menu{
        String NAME = "name";
        String COMPONENT = "component";
        String TITLE = "title";
    }

    interface Role{
        String ROLE_NAME = "role_name";
        String SUMMARY = "summary";
    }

    interface RoleMenu{
        String MENU_ID = "menu_id";
    }

    interface RoleAdmin{
        String ADMIN_ID = "admin_id";
    }
}
