package com.myblog.service.base.common;

public interface Constants {

    interface Symbol {
        String COMMA1 = ",";
        String COMMA2 = "-";
        String COMMA3 = ".";
        String COMMA4 = "/";
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
        String URL = "url";
        String SYS = "sys";
        String CPU = "cpu";
        String MEMORY = "memory";
        String SWAP = "swap";
        String DISK = "disk";
        String TIME = "time";
    }

    interface SystemConstant {
        String WIN = "win";
    }
}
