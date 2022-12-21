package com.myblog.service.security.handler;

import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.JsonUtils;
import com.myblog.service.security.entity.ExceptionLog;
import com.myblog.service.security.mapper.ExceptionLogMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 错误日志异步落库
 */
@Slf4j
@Component
public class ExceptionLogHandler {

    @Autowired
    private ThreadPoolExecutor exceptionLogQueueSizeTaskPool;

    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    public void saveExceptionLog(Throwable exception, Object param, ServletRequestAttributes servletRequestAttributes, String methodName) {
        exceptionLogQueueSizeTaskPool.submit(new PersistentExceptionLogTask(param, servletRequestAttributes, exception, methodName));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    class PersistentExceptionLogTask implements Runnable {

        private Object param;

        private ServletRequestAttributes servletRequestAttributes;

        private Throwable exception;

        private String methodName;

        @Override
        public void run() {
            try {
                ExceptionLog exceptionLog = new ExceptionLog();
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String ip = IpUtils.getIpAddr(request);
                exceptionLog.setIp(ip);
                exceptionLog.setIpSource(IpUtils.getCityInfo(ip));
                exceptionLog.setParams(JsonUtils.objectToJson(param));
                exceptionLog.setMethod(methodName);
                exceptionLog.setExceptionMessage(exception.getMessage());
                exceptionLog.setExceptionDetails(JSON.toJSONString(exception, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue));
                exceptionLogMapper.insert(exceptionLog);
            } catch (UnknownHostException e) {
                log.error("save exception log failed, exception:", e);
            }
        }
    }
}
