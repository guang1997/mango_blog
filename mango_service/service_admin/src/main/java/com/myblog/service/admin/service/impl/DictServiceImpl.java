package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.admin.controller.AdminController;
import com.myblog.service.admin.entity.Dict;
import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.mapper.DictMapper;
import com.myblog.service.admin.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private static Logger LOGGER = LoggerFactory.getLogger(DictServiceImpl.class);
    /**
     * 分页查询字典信息
     * @param dictDto
     * @return
     */
    @Override
    public Response getDictByPage(DictDto dictDto) {
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

        List<DictDto> dictDtos = toDto(dictPage.getRecords());
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
        if (baseMapper.insert(dict) < 1) {
            LOGGER.error("addDict failed by unknown error, dict:{}", dictDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
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
        Dict tag = toDict(dictDto);
        if (baseMapper.updateById(tag) < 1) {
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
        return null;
    }

    private Dict toDict(DictDto dictDto) {
        Dict dict = new Dict();
        BeanUtil.copyProperties(dictDto, dict);
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
