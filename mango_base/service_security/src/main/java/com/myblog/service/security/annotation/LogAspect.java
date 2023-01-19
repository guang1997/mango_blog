package com.myblog.service.security.annotation;

import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.JsonUtils;
import com.myblog.service.base.util.ValidateUtil;
import com.myblog.service.security.handler.ExceptionLogHandler;
import com.myblog.service.security.handler.WebVisitLogHandler;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import java.util.Objects;

/**
 * 日志切面类
 *
 * @author 李斯特
 * @date 2022/02/21
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private WebVisitLogHandler webVisitLogHandler;

    @Autowired
    private ExceptionLogHandler exceptionLogHandler;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.myblog.service.security.annotation.LogByMethod)")
    public void logPointCut() {
    }

    /**
     * 配置环绕通知
     *
     * @return
     */
    @Around("logPointCut()")
    public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标logger
        Logger classLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        // 获取目标方法名称
        String methodName = joinPoint.getSignature().getName();
        // 获取入参
        String params = getParams(joinPoint.getArgs());
        // 对入参进行校验
        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            log.warn("signature:{} not instance of MethodSignature, methodName:{}", joinPoint.getSignature(), methodName);
            return joinPoint.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 配置了注解并且标识为校验入参的方法才进行入参校验
        LogByMethod logByMethod = methodSignature.getMethod().getAnnotation(LogByMethod.class);
        if (Objects.nonNull(logByMethod) && logByMethod.validate()) {
            Response validateResponse = ValidateUtil.validate(joinPoint.getArgs()[0]);
            if (!validateResponse.getSuccess()) {
                return validateResponse;
            }
        }
        // 保存门户网站浏览日志
        if (!Objects.equals(logByMethod.behavior(), BehaviorEnum.DEFAULT)) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(requestAttributes)) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                webVisitLogHandler.saveWebVisitLog(logByMethod.behavior(), servletRequestAttributes);
            } else {
                log.error("save web visit log failed by requestAttributes is null, methodName:{}, params:{}", methodName, params);
            }
        }

        if (classLogger.isDebugEnabled()) {
            classLogger.debug("receive request:{}, params:{}", methodName, params);
        }
        long start = System.currentTimeMillis();
        Object response = null;
        try {
            response = joinPoint.proceed();
            if (logByMethod.printResponse()) {
                classLogger.info("method:{} invoke success, cost:{}, response:{}", methodName, (System.currentTimeMillis() - start), response);
            }
        } catch (Exception e) {
            classLogger.error("method:{} invoke failed, exception:", methodName, e);
            // 保存异常信息到数据库
            saveExceptionMessage(methodName, joinPoint, params, e);
            if (Objects.nonNull(response)) {
                return response;
            }
            return Response.error();
        }
        return response;
    }

    private void saveExceptionMessage(String methodName, ProceedingJoinPoint joinPoint, String params, Exception e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            String method = methodName;
            if (joinPoint.getSignature() instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                ApiOperation annotation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
                if (Objects.nonNull(annotation)) {
                    method = annotation.value();
                }
            }
            exceptionLogHandler.saveExceptionLog(e, params, servletRequestAttributes, method);
        } else {
            log.error("save exception log failed by requestAttributes is null, methodName:{}, params:{}", methodName, params);
        }
    }

    private String getParams(Object[] args) {
        // ServletRequest不打印
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if (arg instanceof ServletRequest) {
                continue;
            }
            params.append(JsonUtils.objectToJson(arg));
        }
        return params.toString();
    }
}
