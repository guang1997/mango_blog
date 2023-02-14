package com.myblog.service.base.util;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 用于处理validation异常
 *
 * @author 李斯特
 * @date 2022/05/15
 */
public class ValidateUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    public static ResultModel<Object> validate(Object request) throws IllegalAccessException {
        if (Objects.isNull(request)) {
            LOGGER.error("request:[{}] validate failed", request);
            return ResultModel.error().message("参数错误，请检查入参");
        }
        Field[] fields = request.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean isNotNull = field.isAnnotationPresent(NotNull.class);
            if (!isNotNull) {
                continue;
            }
            field.setAccessible(true);
            Object requestParam = field.get(request);
            if (Objects.isNull(requestParam) || StringUtils.isBlank(requestParam.toString())) {
                LOGGER.error("request:[{}] validate failed, param:[{}]", request, field.getName());
                return ResultModel.error().message("参数错误，请检查入参");
            }
        }

        return ResultModel.ok();
    }
}
