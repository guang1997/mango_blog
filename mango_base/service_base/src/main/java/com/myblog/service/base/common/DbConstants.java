package com.myblog.service.base.common;

public interface DbConstants {

    interface Base {
        String isDeleted = "is_deleted";
        String sort = "sort";
    }
    interface Admin{
        String userName = "user_name";
        String password = "pass_word";
        String email = "email";
        String mobile = "mobile";
        String status = "status";
    }
    interface Menu{
        String name = "name";
    }
}
