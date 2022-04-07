package com.myblog.service.security.mapper;

import com.myblog.service.security.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface AdminMapper extends BaseMapper<Admin> {

    int updateByUserName(Admin admin);
}
