package com.myblog.service.base.common;

public interface DbConstants {

    interface Base {
        String isDeleted = "is_deleted";
        String sort = "sort";
        String id = "id";
        String pid = "pid";
    }
    interface Admin{
        String username = "username";
        String password = "password";
        String email = "email";
        String mobile = "mobile";
        String status = "status";
    }
    interface Menu{
        String name = "name";
        String component = "component";
        String title = "title";
    }
}
