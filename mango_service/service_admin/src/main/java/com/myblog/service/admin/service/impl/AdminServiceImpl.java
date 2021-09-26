package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.admin.entity.Admin;
import com.myblog.service.admin.entity.vo.LoginVo;
import com.myblog.service.admin.mapper.AdminMapper;
import com.myblog.service.admin.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.DbCommon;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.JwtHelper;
import com.myblog.service.base.util.JwtInfo;
import com.myblog.service.base.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    /**
     * 登录校验
     *
     * @param loginVo
     * @return
     */
    @Override
    public Response doLogin(LoginVo loginVo) {
        Response response = Response.ok();
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        // 对数据进行校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            LOGGER.error("username or password is null, username:{}", username);
            response.setMessage(ResultCodeEnum.LOGIN_ERROR.getMessage());
            response.setCode(ResultCodeEnum.LOGIN_ERROR.getCode());
            return response;
        }

        // 查询数据库
        Admin admin = baseMapper.selectOne(new QueryWrapper<Admin>().
                eq(DbCommon.Admin.userName, loginVo.getUsername()).
                eq(DbCommon.Admin.password, MD5Util.encrypt(loginVo.getPassword())));
        if (null == admin) {
            LOGGER.error("fail find admin, username:{}", username);
            response.setMessage(ResultCodeEnum.LOGIN_USERNAME_OR_PASSWORD_ERROR.getMessage());
            response.setCode(ResultCodeEnum.LOGIN_USERNAME_OR_PASSWORD_ERROR.getCode());
            return response;
        }
        // 验证成功后生成token信息
        JwtInfo jwtInfo = new JwtInfo(admin.getId(), admin.getUserName(), admin.getAvatar());
        response.data("token", JwtHelper.createToken(jwtInfo));
        return response;
    }
}
