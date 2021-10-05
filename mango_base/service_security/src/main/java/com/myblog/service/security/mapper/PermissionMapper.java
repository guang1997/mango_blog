package com.myblog.service.security.mapper;

import com.myblog.service.security.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * API权限表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> getApiUrlByUserName(@Param("userName") String username);
}
