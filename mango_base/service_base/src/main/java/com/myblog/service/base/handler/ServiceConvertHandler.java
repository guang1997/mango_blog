package com.myblog.service.base.handler;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.base.annotation.service.ServiceContextAware;
import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.DbConverter;
import com.myblog.service.base.util.TwoTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现Db和Dto的相互转换
 *
 * @author 李斯特
 * @date 2022/03/24
 */
@Component
public class ServiceConvertHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceConvertHandler.class);

    @Autowired
    private List<ServiceContextAware> serviceContextAwares;

    private Map<String, TwoTuple<Class<? extends BaseEntity>, Class<? extends BaseDto>>> serviceContextAwareMaps = new HashMap<>();

    @PostConstruct
    public void  init() {
        if (CollectionUtils.isEmpty(serviceContextAwares)) {
            throw new RuntimeException("start failed, serviceContextAwares is empty");
        }
        for (ServiceContextAware service : serviceContextAwares) {
            String serviceName = service.getServiceName();
            serviceContextAwareMaps.put(serviceName, new TwoTuple<>(service.getDbClass(), service.getDtoClass()));
        }
        LOGGER.info("serviceContextAwareMaps:{} init success", serviceContextAwareMaps);
    }

    public List<BaseDto> toDto(List<? extends BaseEntity> entities, String serviceName) throws IllegalAccessException, InstantiationException {
        List<BaseDto> baseDtos = new ArrayList<>();
        TwoTuple<Class<? extends BaseEntity>, Class<? extends BaseDto>> twoTuple = serviceContextAwareMaps.get(serviceName);
        Class<? extends BaseDto> dtoClazz = twoTuple.getSecond();
        for (BaseEntity entity : entities) {
            BaseDto baseDto = dtoClazz.newInstance();
            BeanUtil.copyProperties(entity, baseDto);
            baseDto.setId(entity.getId());
            baseDto.setCreateTime(entity.getCreateTime());
            baseDtos.add(baseDto);
        }
        return baseDtos;
    }
}
