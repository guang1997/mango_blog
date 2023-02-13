package com.myblog.service.web.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.Tag;
import com.myblog.service.web.entity.dto.TagDto;
import com.myblog.service.web.mapper.TagMapper;
import com.myblog.service.web.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Value("${web.tagPage:1}")
    private Integer sortPage;

    @Value("${web.tagSize:100}")
    private Integer sortSize;

    @Override
    public Map<String, Object> getTagByPage(TagDto tagDto) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        if (Objects.nonNull(tagDto.getQueryAll()) && tagDto.getQueryAll()) {
            List<TagDto> tagDtos = this.toDtoList(baseMapper.selectList(null), TagDto.class);
            resultMap.put(Constants.ReplyField.DATA, tagDtos);
            resultMap.put(Constants.ReplyField.TOTAL, tagDtos.size());
            return resultMap;
        }

        int page = tagDto.getPage();
        int size = tagDto.getSize();
        // 首页查询最新分类
        if (BooleanUtil.isTrue(tagDto.getQueryLatest())) {
            page = sortPage;
            size = sortSize;
        }

        Page<Tag> tagPage = new Page<>(page, size);
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(tagPage, queryWrapper);

        resultMap.put(Constants.ReplyField.DATA, this.toDtoList(tagPage.getRecords(), TagDto.class));
        resultMap.put(Constants.ReplyField.TOTAL, tagPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, page);
        resultMap.put(Constants.ReplyField.SIZE, size);
        return resultMap;
    }
}
