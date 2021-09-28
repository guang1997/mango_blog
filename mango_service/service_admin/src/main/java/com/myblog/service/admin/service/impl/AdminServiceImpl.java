package com.myblog.service.admin.service.impl;

import com.myblog.service.admin.entity.Admin;
import com.myblog.service.admin.mapper.AdminMapper;
import com.myblog.service.admin.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
