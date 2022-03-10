package com.myblog.service.base.common;

public interface DbConstants {

    interface Base {
        String IS_DELETED = "is_deleted";
        String SORT = "sort";
        String ID = "id";
        String PID = "pid";
        String CREATE_TIME = "create_time";
        String UPDATE_TIME = "update_time";
    }

    interface Admin{
        String USERNAME = "username";
        String PASSWORD = "password";
        String EMAIL = "email";
        String MOBILE = "mobile";
        String STATUS = "status";
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
}
