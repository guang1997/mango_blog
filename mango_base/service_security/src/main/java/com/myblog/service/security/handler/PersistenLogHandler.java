package com.myblog.service.security.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.security.annotation.LogAspect;
import com.myblog.service.security.entity.WebVisit;
import com.myblog.service.security.service.WebVisitService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 门户网站访问日志异步落库
 */
@Component
public class PersistenLogHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(PersistenLogHandler.class);

    private static final ExecutorService persistenLogExecutor = new ThreadPoolExecutor(
            1,
            5,
            120,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("persisten-log").build()
    );

    @Autowired
    private WebVisitService webVisitService;


    public void saveWebVisitLog(BehaviorEnum behavior, ServletRequestAttributes servletRequestAttributes) {
        persistenLogExecutor.submit(new PersistenLogTask(behavior, servletRequestAttributes));
    }

    class PersistenLogTask implements Runnable {
        private BehaviorEnum behavior;
        private ServletRequestAttributes servletRequestAttributes;

        public PersistenLogTask() {
        }

        public PersistenLogTask(BehaviorEnum behavior, ServletRequestAttributes servletRequestAttributes) {
            this.behavior = behavior;
            this.servletRequestAttributes = servletRequestAttributes;
        }

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
            } catch (Exception e) {
                LOGGER.error("save web visit log failed by exception:", e);
            }
        }
    }
}
