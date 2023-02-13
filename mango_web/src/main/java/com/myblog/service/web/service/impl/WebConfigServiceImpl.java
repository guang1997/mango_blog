package com.myblog.service.web.service.impl;

import java.util.Arrays;
import java.util.Objects;

import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Constants.ReplyField;
import com.myblog.service.base.common.Constants.Symbol;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.exception.BusinessException;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.web.entity.WebConfig;
import com.myblog.service.web.entity.dto.WebConfigDto;
import com.myblog.service.web.mapper.WebConfigMapper;
import com.myblog.service.web.service.WebConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * web配置表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-31
 */
@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    @Override
    public WebConfigDto getWebConfig() throws Exception {
        WebConfig webConfig = baseMapper.selectOne(null);
        if (Objects.isNull(webConfig)) {
            throw new BusinessException("未找到配置信息");
        }

        WebConfigDto webConfigDto = this.toDto(webConfig, WebConfigDto.class);
        String rollingSentences = webConfig.getRollingSentences();
        if (StringUtils.isNotBlank(rollingSentences)) {
            webConfigDto.setRollingSentences(Arrays.asList(rollingSentences.split(Symbol.COMMA7)));
        }
        return webConfigDto;
    }
}
