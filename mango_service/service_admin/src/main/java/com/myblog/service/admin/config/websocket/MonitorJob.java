package com.myblog.service.admin.config.websocket;

import com.myblog.service.admin.controller.AdminController;
import com.myblog.service.admin.service.MonitorService;
import com.myblog.service.base.util.JsonUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 服务监控页面定时任务
 *
 * @author 李斯特
 * 2022年4月4日
 */
@Data
@Component
public class MonitorJob {

    private static Logger LOGGER = LoggerFactory.getLogger(MonitorJob.class);

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private WebScoketServer webScoketServer;

    @Scheduled(cron = "0/3 * * * * ?")
    public void test() {
        try {
            // 如果有用户打开了服务监控页面，那么使用websocket往所有连接到websocket的页面发送消息
            if (webScoketServer.getOnlineCount() > 0) {
                String response = JsonUtils.objectToJson(monitorService.getServers());
                webScoketServer.sendMessage(response);
                LOGGER.trace("monitor websocket sendMessage success, response:{}", response);
            }
        } catch (Exception e) {
            LOGGER.error("monitor websocket sendMessage failed, exception:", e);
        }
    }
}
