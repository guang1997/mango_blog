package com.myblog.service.security.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.security.entity.WebVisit;
import com.myblog.service.security.mapper.WebVisitMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 门户网站访问日志异步落库
 */
@Slf4j
@Component
public class WebVisitLogHandler {

    @Autowired
    private ThreadPoolExecutor webVisitQueueSizeTaskPool;
    @Autowired
    private WebVisitMapper webVisitMapper;


    public void saveWebVisitLog(BehaviorEnum behavior, ServletRequestAttributes servletRequestAttributes) {
        webVisitQueueSizeTaskPool.submit(new PersistentWebVisitTask(behavior, servletRequestAttributes));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    class PersistentWebVisitTask implements Runnable {
        private BehaviorEnum behavior;
        private ServletRequestAttributes servletRequestAttributes;

        @Override
        public void run() {
            try {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                WebVisit webVisit = new WebVisit();
                webVisit.setBehavior(behavior.getBehavior());
                webVisit.setIsMenu(behavior.getIsMenu());
                webVisit.setContent(behavior.getContent());
                webVisit.setRequestTime(new Date());
                String ip = IpUtils.getIpAddr(request);
                webVisit.setIp(ip);
                String addresses = IpUtils.getAddresses(ip, "utf-8");
                if (StringUtils.isNotBlank(addresses)) {
                    webVisit.setIpSource(addresses);
                }
                Map<String, String> osAndBrowserInfo = IpUtils.getOsAndBrowserInfo(request);
                webVisit.setOs(osAndBrowserInfo.get(Constants.ReplyField.OS));
                webVisit.setBrowser(osAndBrowserInfo.get(Constants.ReplyField.BROWSER));
                webVisitMapper.insert(webVisit);
            } catch (Exception e) {
                log.error("save web visit log failed by exception:", e);
            }
        }
    }
}
