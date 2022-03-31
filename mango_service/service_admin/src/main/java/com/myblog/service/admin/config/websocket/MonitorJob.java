package com.myblog.service.admin.config.websocket;

import com.myblog.service.admin.service.MonitorService;
import com.myblog.service.admin.service.WebScoketServer;
import com.myblog.service.base.util.JsonUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
public class MonitorJob {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private WebScoketServer webScoketServer;

    @Scheduled(cron = "0/5 * * * * ?")
    public void test() {
        try {
            System.out.println(webScoketServer.getOnlineCount());
            if (webScoketServer.getOnlineCount() > 0) {
                System.out.println("sendMessage");
                webScoketServer.sendMessage(JsonUtils.objectToJson(monitorService.getServers()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
