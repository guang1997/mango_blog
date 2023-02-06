package com.myblog.service.admin.service.impl;

import java.text.ParseException;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.service.ExceptionLogService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.DbConstants.Base;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.ExceptionLog;
import com.myblog.service.security.entity.dto.ExceptionLogDto;
import com.myblog.service.security.mapper.ExceptionLogMapper;
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
    public Map<String, Object> getExceptionByPage(ExceptionLogDto exceptionLogDto) throws Exception {
        QueryWrapper<ExceptionLog> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(exceptionLogDto.getCreateTimes()) && Objects.equals(2, exceptionLogDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(exceptionLogDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(exceptionLogDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        queryWrapper.orderByDesc(Base.CREATE_TIME);
        Page<ExceptionLog> exceptionLogPage = new Page<>(exceptionLogDto.getPage(), exceptionLogDto.getSize());
        baseMapper.selectPage(exceptionLogPage, queryWrapper);
        List<ExceptionLogDto> exceptionLogDtos = this.toDtoList(exceptionLogPage.getRecords(), ExceptionLogDto.class);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constants.ReplyField.DATA, exceptionLogDtos);
        resultMap.put(Constants.ReplyField.TOTAL, exceptionLogPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, exceptionLogDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, exceptionLogDto.getSize());
        return resultMap;
    }
}
