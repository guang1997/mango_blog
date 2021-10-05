package com.myblog.service.security.config.service;

import com.myblog.service.base.exception.MyaccessDeniedException;
import com.myblog.service.security.entity.Permission;
import com.myblog.service.security.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component("dynamicPermissionService")
public class DynamicPermissionService {

    private static Logger LOGGER = LoggerFactory.getLogger(DynamicPermissionService.class);

    @Autowired
    private PermissionService permissionService;

    /**
     * 判断有访问API的权限
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean checkPermisstion(HttpServletRequest request,
                                    Authentication authentication) {

        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;
        if (principal instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) principal;
            //得到当前的账号
            String username = userDetails.getUsername();

            //通过角色获取资源鉴权
            List<Permission> apiUrls = permissionService.getApiUrlByUserName(username);

            AntPathMatcher antPathMatcher = new AntPathMatcher();
            //当前访问路径
            String requestURI = request.getRequestURI();
            //提交类型
            String urlMethod = request.getMethod();
            LOGGER.debug("userName:{}, requestUrl:{}, urlMethod:{}", username, requestURI, urlMethod);

            //判断当前路径中是否在资源鉴权中
            hasPermission = apiUrls.stream().anyMatch(item -> {
                //判断URL是否匹配
                boolean hashAntPath = antPathMatcher.match(item.getApiUrl(), requestURI);

                //判断请求方式是否和数据库中匹配（数据库存储：GET,POST,PUT,DELETE）
                String dbMethod = item.getApiMethod() == null ? "" : item.getApiMethod();
                int hasMethod = dbMethod.indexOf(urlMethod);
                LOGGER.debug("userName:{}, hashAntPath:{}, hasMethod:{}", username, hashAntPath, hasMethod);
                //两者都成立，返回真，否则返回假
                return hashAntPath && (hasMethod != -1);
            });
//            if (hasPermission) {
//                return hasPermission;
//            }else {
//                throw  new MyaccessDeniedException("您没有访问该API的权限！");
//            }

        }
//        else{

//            throw  new MyaccessDeniedException("不是UserDetails类型！");
//        }
        return hasPermission;
    }
}

























