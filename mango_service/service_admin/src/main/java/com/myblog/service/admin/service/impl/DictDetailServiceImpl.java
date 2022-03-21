package com.myblog.service.admin.service.impl;

import com.myblog.service.admin.entity.DictDetail;
import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.admin.mapper.DictDetailMapper;
import com.myblog.service.admin.service.DictDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.Response;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 字典详细数据表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Service
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    @Override
    public Response getDictDetailByPage(DictDetailDto dictDetailDto) {
        return null;
    }

    @Override
    public Response editDictDetail(DictDetailDto dictDetailDto) {
        return null;
    }

    @Override
    public Response delDictDetails(Set<String> ids) {
        return null;
    }

    @Override
    public Response addDictDetail(DictDetailDto dictDetailDto) {
        return null;
    }
}
