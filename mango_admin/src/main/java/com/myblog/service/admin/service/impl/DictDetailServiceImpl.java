package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.DictDetail;
import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.admin.mapper.DictDetailMapper;
import com.myblog.service.admin.service.DictDetailService;
import com.myblog.service.base.common.*;
import com.myblog.service.base.util.JsonUtils;
import com.myblog.service.base.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 字典详细数据表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Slf4j
@Service
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页查询字典详情
     * @param dictDetailDto
     * @return
     */
    @Override
    public Response getDictDetailByPage(DictDetailDto dictDetailDto) throws Exception{
        Response response = Response.ok();
        QueryWrapper<DictDetail> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(dictDetailDto.getPage())) {
            page = dictDetailDto.getPage();
        }
        if (Objects.nonNull(dictDetailDto.getSize())) {
            size = dictDetailDto.getSize();
        }
        if (StringUtils.isNotBlank(dictDetailDto.getDictId())) {
            queryWrapper.eq(DbConstants.DictDetail.DICT_ID, dictDetailDto.getDictId());
        }
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        queryWrapper.orderByDesc(DbConstants.Base.SORT);
        Page<DictDetail> dictDetailPage = new Page<>(page, size);

        baseMapper.selectPage(dictDetailPage, queryWrapper);

        List<DictDetailDto> dictDetailDtos = this.toDtoList(dictDetailPage.getRecords(), DictDetailDto.class);
        response.data(Constants.ReplyField.DATA, dictDetailDtos);
        response.data(Constants.ReplyField.TOTAL, dictDetailPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    @Override
    public Response editDictDetail(DictDetailDto dictDetailDto) throws Exception{
        QueryWrapper<DictDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.DictDetail.DICT_ID, dictDetailDto.getDictId());
        DictDetail dictDetail = this.toDb(dictDetailDto, DictDetail.class);
        if (baseMapper.updateById(dictDetail) < 1) {
            log.error("editDictDetail failed by unknown error, dictDetail:{}", dictDetail);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    @Override
    public Response delDictDetails(Set<String> ids) throws Exception{
        for (String id : ids) {
            if (baseMapper.deleteById(id) < 1) {
                log.error("delDictDetails failed by unknown error, dictDetailId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        return Response.ok();
    }

    @Override
    public Response addDictDetail(DictDetailDto dictDetailDto) throws Exception{
        QueryWrapper<DictDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.DictDetail.DICT_LABEL, dictDetailDto.getDictLabel());
        List<DictDetail> dbDictDetails = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(dbDictDetails)) {
            boolean present = dbDictDetails.stream().anyMatch(db -> Objects.equals(dictDetailDto.getDictId(), db.getDictId()));
            if (present) {
                log.error("addDictDetail failed, dictLabel is already exist, dictDetailDto:{}", dictDetailDto);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }
        DictDetail dictDetail = this.toDb(dictDetailDto, DictDetail.class);
        // 如果已经有同名且被删除的字典，那么只更新
        dictDetail.setCreateTime(new Date());
        dictDetail.setUpdateTime(new Date());
        if (baseMapper.updateByDictLabel(dictDetail) < 1) {
            if (baseMapper.insert(dictDetail) < 1) {
                log.error("addDictDetail failed by unknown error, dictDetail:{}", dictDetail);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }
        return Response.ok();
    }

    /**
     * 根据字典名获取字典详细信息
     * @param dictName
     * @return
     */
    @Override
    public Response getDetailsByDictName(String dictName) {
        // 先从redis中获取数据，如果取到数据那么直接返回
        String redisStr = redisUtil.get(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictName);
        if (!StringUtils.isBlank(redisStr)) {
            Map<String, Object> result = JsonUtils.jsonToMap(redisStr);
            return Response.ok().data(result);
        }
        List<DictDetail> detailList = baseMapper.getDetailsByDictName(dictName);
        if (CollectionUtils.isEmpty(detailList)) {
            log.warn("getDetailsByDictName failed, cannot find dictDetails from db, dictName:{}", dictName);
            return Response.ok();
        }
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.ReplyField.DATA, detailList);
        redisUtil.setEx(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictName, JsonUtils.objectToJson(result), 1, TimeUnit.DAYS);
        return Response.ok().data(result);
    }
}
