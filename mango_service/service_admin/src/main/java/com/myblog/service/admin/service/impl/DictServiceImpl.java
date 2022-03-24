package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.admin.controller.AdminController;
import com.myblog.service.admin.entity.Dict;
import com.myblog.service.admin.entity.DictDetail;
import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.mapper.DictDetailMapper;
import com.myblog.service.admin.mapper.DictMapper;
import com.myblog.service.admin.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.annotation.service.ServiceContext;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.base.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@ServiceContext(serviceName = "DictService", dbClazz = Dict.class, dtoClazz = DictDto.class)
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private static Logger LOGGER = LoggerFactory.getLogger(DictServiceImpl.class);

    @Autowired
    private DictDetailMapper dictDetailMapper;

    @Autowired
    private ServiceConvertHandler serviceConvertHandler;


    /**
     * 分页查询字典信息
     * @param dictDto
     * @return
     */
    @Override
    public Response getDictByPage(DictDto dictDto) throws Exception{
        Response response = Response.ok();
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(dictDto.getPage())) page = dictDto.getPage();
        if (Objects.nonNull(dictDto.getSize())) size = dictDto.getSize();
        if (StringUtils.isNotBlank(dictDto.getBlurry())) {
            queryWrapper.like(DbConstants.Dict.DICT_NAME, dictDto.getBlurry())
                    .or()
                    .like(DbConstants.Base.SUMMARY, dictDto.getBlurry());
        }
        queryWrapper.eq(DbConstants.Base.IS_DELETED, "0");
        queryWrapper.orderByDesc(DbConstants.Base.SORT);
        Page<Dict> dictPage = new Page<>(page, size);

        baseMapper.selectPage(dictPage, queryWrapper);

        List<BaseDto> dictDtos = serviceConvertHandler.toDto(dictPage.getRecords(), this.getServiceName());
        response.data(Constants.ReplyField.DATA, dictDtos);
        response.data(Constants.ReplyField.TOTAL, dictPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 添加字典
     * @param dictDto
     * @return
     */
    @Override
    public Response addDict(DictDto dictDto) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Dict.DICT_NAME, dictDto.getDictName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            LOGGER.error("addDict failed, dictName is already exist, dictDto:{}", dictDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Dict dict = toDict(dictDto);
        // 如果已经有同名且被删除的字典，那么只更新
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        if (baseMapper.updateByDictName(dict) < 1) {
            if (baseMapper.insert(dict) < 1) {
                LOGGER.error("addDict failed by unknown error, dict:{}", dictDto);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }

        return Response.ok();
    }

    /**
     * 修改字典
     * @param dictDto
     * @return
     */
    @Override
    public Response editDict(DictDto dictDto) {
        Dict dict = toDict(dictDto);
        if (baseMapper.updateById(dict) < 1) {
            LOGGER.error("editDict failed by unknown error, dict:{}", dictDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 删除字典
     * @param ids
     * @return
     */
    @Override
    public Response delDict(Set<String> ids) {
        for (String id : ids) {
            // 删除字典时需要删除对应的字典详情
            UpdateWrapper<DictDetail> dictDetailUpdateWrapper = new UpdateWrapper<>();
            dictDetailUpdateWrapper.eq(DbConstants.DictDetail.DICT_ID, id);
            dictDetailMapper.delete(dictDetailUpdateWrapper);
            // 然后删除对应的字典
            if (baseMapper.deleteById(id) < 1) {
                LOGGER.error("delDict failed by unknown error, dictId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        return Response.ok();
    }

    private Dict toDict(DictDto dictDto) {
        Dict dict = new Dict();
        BeanUtil.copyProperties(dictDto, dict);
        if (StringUtils.isNotBlank(dictDto.getId())) {
            dict.setId(dictDto.getId());
        }
        return dict;
    }

    private List<DictDto> toDto(List<Dict> dicts) {
        List<DictDto> dictDtos = new ArrayList<>();
        for (Dict dict : dicts) {
            DictDto dictDto = new DictDto();
            BeanUtil.copyProperties(dict, dictDto);
            dictDto.setId(dict.getId());
            dictDto.setCreateTime(dict.getCreateTime());
            dictDtos.add(dictDto);
        }
        return dictDtos;
    }
}
