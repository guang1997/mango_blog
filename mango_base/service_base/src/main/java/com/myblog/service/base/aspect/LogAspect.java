package com.myblog.service.base.aspect;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.JsonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;

/**
 * 日志切面类
 *
 * @author 李斯特
 * @date 2022/02/21
 */
@Aspect
@Component
public class LogAspect {

    private static Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.myblog.service.base.annotation.aspect.LogByMethod)")
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
        // TODO 对入参进行校验
        if (classLogger.isDebugEnabled()) {
            classLogger.debug("recieve request:{}, params:{}", methodName, params);
        }
        long start = System.currentTimeMillis();
        Object response = null;
        try {
            response = joinPoint.proceed();
            classLogger.info("method:{} invoke success, cost:{}, response:{}", methodName, (System.currentTimeMillis() - start), response);
        } catch (Exception e) {
            classLogger.error("method:" + methodName + " invoke failed, exception:", e);
            return Response.error().message(e.getMessage());
        }
        return response;
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
