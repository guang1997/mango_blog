package com.myblog.service.admin.config.websocket;

import com.myblog.service.admin.service.MonitorService;
import com.myblog.service.base.util.JsonUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
public class MonitorJob {

    private static Logger LOGGER = LoggerFactory.getLogger(MonitorJob.class);

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private WebScoketServer webScoketServer;

    @Scheduled(cron = "0/3 * * * * ?")
    public void sendMonitorMessage() {
        try {
            if (webScoketServer.getOnlineCount() > 0) {
                String response = JsonUtils.objectToJson(monitorService.getServers());
                webScoketServer.sendMessage(response);
                LOGGER.trace("send monitor message to web success, response:{}", response);
            }
        } catch (Exception e) {
            LOGGER.error("send monitor message to web failed, exception:", e);
        }
    }
}
