package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Dict;
import com.myblog.service.admin.entity.DictDetail;
import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.admin.mapper.DictDetailMapper;
import com.myblog.service.admin.mapper.DictMapper;
import com.myblog.service.admin.service.DictService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictDetailMapper dictDetailMapper;

    /**
     * 分页查询字典信息
     * @param dictDto
     * @return
     */
    @Override
    public Map<String, Object> getDictByPage(DictDto dictDto) throws Exception{
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dictDto.getBlurry())) {
            queryWrapper.like(DbConstants.Dict.DICT_NAME, dictDto.getBlurry())
                    .or()
                    .like(DbConstants.Base.SUMMARY, dictDto.getBlurry());
        }
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        queryWrapper.orderByDesc(DbConstants.Base.SORT);
        Page<Dict> dictPage = new Page<>(dictDto.getPage(), dictDto.getSize());

        baseMapper.selectPage(dictPage, queryWrapper);

        List<DictDto> dictDtos = this.toDtoList(dictPage.getRecords(), DictDto.class);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constants.ReplyField.DATA, dictDtos);
        resultMap.put(Constants.ReplyField.TOTAL, dictPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, dictDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, dictDto.getSize());
        return resultMap;
    }

    /**
     * 添加字典
     * @param dictDto
     * @return
     */
    @Override
    public Boolean addDict(DictDto dictDto) throws Exception{
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Dict.DICT_NAME, dictDto.getDictName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            throw new BusinessException("字典名称{" + dictDto.getDictName() +"}已存在");
        }
        Dict dict = this.toDb(dictDto, Dict.class);
        // 如果已经有同名且被删除的字典，那么只更新
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        if (baseMapper.updateByDictName(dict) < 1) {
            if (baseMapper.insert(dict) < 1) {
                log.error("addDict failed by unknown error, dict:{}", dict);
                return false;
            }
        }

        return true;
    }

    /**
     * 修改字典
     * @param dictDto
     * @return
     */
    @Override
    public Boolean editDict(DictDto dictDto) throws Exception{
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Dict.DICT_NAME, dictDto.getDictName());
        List<Dict> dicts = baseMapper.selectList(queryWrapper);
        if (dicts.size() > 0) {
            for (Dict dict : dicts) {
                if (!Objects.equals(dictDto.getId(), dict.getId())) {
                    throw new BusinessException("更新失败, 已存在相同名称的字典");
                }
            }
        }
        Dict dict = this.toDb(dictDto, Dict.class);
        if (baseMapper.updateById(dict) < 1) {
            log.error("editDict failed by unknown error, dict:{}", dict);
            return false;
        }
        return true;
    }

    /**
     * 删除字典
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delDict(Set<String> ids) throws Exception{
        for (String id : ids) {
            // 删除字典时需要删除对应的字典详情
            UpdateWrapper<DictDetail> dictDetailUpdateWrapper = new UpdateWrapper<>();
            dictDetailUpdateWrapper.eq(DbConstants.DictDetail.DICT_ID, id);
            dictDetailMapper.delete(dictDetailUpdateWrapper);
            // 然后删除对应的字典
            if (baseMapper.deleteById(id) < 1) {
                log.error("delDict failed by unknown error, dictId:{}", id);
                throw new BusinessException("删除失败");
            }
        }
        return true;
    }

}
