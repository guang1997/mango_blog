package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.User;
import com.myblog.service.admin.entity.dto.UserDto;
import com.myblog.service.admin.mapper.UserMapper;
import com.myblog.service.admin.service.UserService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.service.impl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-18
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Response getUserByPage(UserDto userDto) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 10;
        if (Objects.nonNull(userDto.getPage())) page = userDto.getPage();
        if (Objects.nonNull(userDto.getSize())) size = userDto.getSize();
        Page<User> userPage = new Page<>(page, size);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userDto.getBlurry())) {
            queryWrapper.like(DbConstants.User.USERNAME, userDto.getBlurry())
                    .or()
                    .like(DbConstants.User.NICKNAME, userDto.getBlurry());
        }
        if (Objects.nonNull(userDto.getCommentStatus())) {
            queryWrapper.eq(DbConstants.User.COMMENT_STATUS, userDto.getCommentStatus());
        }
        if (!CollectionUtils.isEmpty(userDto.getLastLoginTimes()) && Objects.equals(2, userDto.getLastLoginTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(userDto.getLastLoginTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(userDto.getLastLoginTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Admin.LAST_LOGIN_TIME, beginDate, endDate);
        }
        baseMapper.selectPage(userPage, queryWrapper);
        List<UserDto> userDtos = this.toDtoList(userPage.getRecords(), UserDto.class);

        response.data(Constants.ReplyField.DATA, userDtos);
        response.data(Constants.ReplyField.TOTAL, userPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    @Override
    public Response editUser(UserDto userDto) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(DbConstants.User.EMAIL, userDto.getEmail());
        updateWrapper.eq(DbConstants.User.USERNAME, userDto.getUsername());
        User user = new User();
        user.setId(userDto.getId());
        user.setCommentStatus(userDto.getCommentStatus());
        if (baseMapper.update(user, updateWrapper) < 1) {
            log.error("editUser failed by unknown error, user:{}", userDto);
            return Response.error();
        }
        return Response.ok();
    }
}
