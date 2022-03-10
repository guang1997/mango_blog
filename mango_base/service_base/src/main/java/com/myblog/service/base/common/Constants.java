package com.myblog.service.base.common;

public interface Constants {

    interface Status {
        int ACTIVATED = 1;
        int FREEZED = 0;
    }

    interface Symbol {
        String COMMA1 = ",";
        String COMMA2 = "-";
    }

    interface ReplyField {
        String ID = "id";
        String TOKEN = "token";
        String ROLES = "roles";
        String AVATAR = "avatar";
        String USER_NAME = "username";
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
    }
}
