package com.myblog.service.admin.service.impl;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.util.BooleanUtil;
import com.myblog.service.admin.entity.WebConfig;
import com.myblog.service.admin.entity.dto.WebConfigDto;
import com.myblog.service.admin.mapper.WebConfigMapper;
import com.myblog.service.admin.service.WebConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Constants.ReplyField;
import com.myblog.service.base.common.Constants.Symbol;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * web配置表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-30
 */
@Slf4j
@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    @Override
    public WebConfigDto getWebConfig() throws Exception {
        WebConfig webConfig = baseMapper.selectOne(null);
        if (Objects.isNull(webConfig)) {
            return new WebConfigDto();
        }
        WebConfigDto webConfigDto = this.toDto(webConfig, WebConfigDto.class);
        String rollingSentences = webConfig.getRollingSentences();
        if (StringUtils.isNotBlank(rollingSentences)) {
            webConfigDto.setRollingSentences(Arrays.asList(rollingSentences.split(Symbol.COMMA7)));
        }
        return webConfigDto;
    }

    @Override
    public Boolean editWebConfig(WebConfigDto webConfigDto) throws Exception{
        if (BooleanUtil.isTrue(webConfigDto.getDeleteSentence())) {
            WebConfig webConfig = baseMapper.selectById(webConfigDto.getId());
            if (Objects.isNull(webConfig)) {
                log.error("cannot find webConfig by id:{}", webConfigDto.getId());
                return false;
            }
            String[] split = webConfig.getRollingSentences().split(Symbol.COMMA7);
            String newSentences = Arrays.stream(split)
                .filter(sentence -> !webConfigDto.getRollingSentences().contains(sentence))
                .collect(Collectors.joining(Symbol.COMMA7));
            webConfig.setRollingSentences(newSentences);
            if (baseMapper.updateById(webConfig) < 1) {
                if (baseMapper.insert(webConfig) < 1) {
                    log.error("editWebConfig failed by unknown error, webConfig:{}", webConfig);
                    return false;
                }
            }
            return true;
        }
        WebConfig webConfig = this.toDb(webConfigDto, WebConfig.class);
        if (!CollectionUtils.isEmpty(webConfigDto.getRollingSentences())) {
            webConfig.setRollingSentences(String.join(Symbol.COMMA7, webConfigDto.getRollingSentences()));
        }
        if (baseMapper.updateById(webConfig) < 1) {
            if (baseMapper.insert(webConfig) < 1) {
                log.error("editWebConfig failed by unknown error, webConfig:{}", webConfig);
                return false;
            }

        }
        return true;
    }
}
