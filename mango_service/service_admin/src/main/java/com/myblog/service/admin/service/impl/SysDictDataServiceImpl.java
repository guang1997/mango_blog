package com.myblog.service.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.myblog.service.admin.controller.MenuController;
import com.myblog.service.admin.entity.SysDictData;
import com.myblog.service.admin.mapper.SysDictDataMapper;
import com.myblog.service.admin.service.SysDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.JsonUtils;
import com.myblog.service.base.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-08
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    private static Logger LOGGER = LoggerFactory.getLogger(SysDictDataServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    /**
     * 根据单个dictType获取具体字典值
     * @param dictType
     * @return
     */
    @Override
    public Response getListByDictType(String dictType) {
        // 先从redis中获取数据，如果取到数据那么直接返回
        String redisStr = redisUtil.get(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictType);
        if (!StringUtils.isEmpty(redisStr)) {
            Map<String, Object> result = JsonUtils.jsonToMap(redisStr);
            return Response.ok().data(result);
        }
        List<SysDictData> dictDataList = sysDictDataMapper.getListByDictType(dictType);
        if (CollectionUtils.isEmpty(dictDataList)) {
            LOGGER.warn("getListByDictType cannot find dictDate from db, dictType:{}", dictType);
            return Response.ok();
        }
        String defaultValue = null;
        for (SysDictData sysDictData : dictDataList) {
            // 获取默认值
            if (sysDictData.getIsDefault()) {
                defaultValue = sysDictData.getDictValue();
                break;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.ReplyField.DEFAULT_VALUE, defaultValue);
        result.put(Constants.ReplyField.LIST, dictDataList);
        redisUtil.setEx(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictType, JsonUtils.objectToJson(result), 1, TimeUnit.DAYS);
        return Response.ok().data(result);
    }

    /**
     * 根据多个dictType获取字典值
     * @param dictTypes
     * @return
     */
    @Override
    public Response getListByDictTypeList(List<String> dictTypes) {
        // 先从redis中获取数据，如果取到所有的数据再返回
        Map<String, Object> resultMap = new HashMap<>();
        for (String dictType : dictTypes) {
            String redisStr = redisUtil.get(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictType);
            if (!StringUtils.isEmpty(redisStr)) {
                Map<String, Object> result = JsonUtils.jsonToMap(redisStr);
                resultMap.put(dictType, result);
                dictTypes.remove(dictType);
            }
        }
        if (dictTypes.size() <= 0) {
            return Response.ok().data(resultMap);
        }
        for (String dictType : dictTypes) {
            List<SysDictData> dictDataList = sysDictDataMapper.getListByDictType(dictType);
            if (CollectionUtils.isEmpty(dictDataList)) {
                LOGGER.warn("getListByDictTypeList cannot find dictDate from db, dictType:{}", dictType);
                continue;
            }
            String defaultValue = null;
            for (SysDictData sysDictData : dictDataList) {
                // 获取默认值
                if (sysDictData.getIsDefault()) {
                    defaultValue = sysDictData.getDictValue();
                    break;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put(Constants.ReplyField.DEFAULT_VALUE, defaultValue);
            result.put(Constants.ReplyField.LIST, dictDataList);
            resultMap.put(dictType, result);
            redisUtil.setEx(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictType, JsonUtils.objectToJson(result), 1, TimeUnit.DAYS);
        }

        return Response.ok().data(resultMap);
    }
}
