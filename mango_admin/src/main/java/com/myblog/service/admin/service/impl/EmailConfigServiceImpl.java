package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.EmailConfig;
import com.myblog.service.admin.mapper.EmailConfigMapper;
import com.myblog.service.admin.service.EmailConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮箱配置 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-04
 */
@Service
public class EmailConfigServiceImpl extends ServiceImpl<EmailConfigMapper, EmailConfig> implements EmailConfigService {

}
