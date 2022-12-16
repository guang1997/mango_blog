package com.myblog.service.admin.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.ExceptionLog;
import com.myblog.service.admin.entity.Link;
import com.myblog.service.admin.entity.dto.ExceptionLogDto;
import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.mapper.ExceptionLogMapper;
import com.myblog.service.admin.service.ExceptionLogService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.DbConstants.Base;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 异常信息表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-12-16
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Override
    public Response getExceptionByPage(ExceptionLogDto exceptionLogDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<ExceptionLog> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(exceptionLogDto.getPage())) {
            page = exceptionLogDto.getPage();
        }
        if (Objects.nonNull(exceptionLogDto.getSize())) {
            size = exceptionLogDto.getSize();
        }
        if (!CollectionUtils.isEmpty(exceptionLogDto.getCreateTimes()) && Objects.equals(2, exceptionLogDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(exceptionLogDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(exceptionLogDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        queryWrapper.orderByDesc(Base.CREATE_TIME);
        Page<ExceptionLog> exceptionLogPage = new Page<>(page, size);
        baseMapper.selectPage(exceptionLogPage, queryWrapper);
        List<ExceptionLogDto> exceptionLogDtos = this.toDtoList(exceptionLogPage.getRecords(), ExceptionLogDto.class);
        response.data(Constants.ReplyField.DATA, exceptionLogDtos);
        response.data(Constants.ReplyField.TOTAL, exceptionLogPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }
}
