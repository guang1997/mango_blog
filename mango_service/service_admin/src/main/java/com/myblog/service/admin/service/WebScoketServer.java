package com.myblog.service.admin.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.JsonUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Component
@ServerEndpoint("/admin/websocket")
public class WebScoketServer {

    private static Logger LOGGER = LoggerFactory.getLogger(WebScoketServer.class);

//    private static MonitorService monitorService;
//
//    @Autowired
//    public void setMonitorService(MonitorService monitorService){
//        WebScoketServer.monitorService = monitorService;
//    }
    /**
     * 调度周期，毫秒
     */
    private int perid = 5000;

    /**
     * 触发延迟，毫秒
     */
    private int delay = 0;

    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 用来存放每个客户端对应的链接对象。
     */
    private static CopyOnWriteArraySet<Session> allClients = new CopyOnWriteArraySet<>();

//    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private boolean isInit = true;

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        allClients.add(session);
//        System.out.println(monitorService);
//        if (isInit) {
//            executorService.scheduleAtFixedRate(this, this.getDelay(), this.getPerid(), TimeUnit.MICROSECONDS);
//        }
        isInit = false;
        int onlineCount = WebScoketServer.onlineCount.incrementAndGet();
        LOGGER.info("connent to webSocket, id:{}, onlineCount:{}", session.getId(), onlineCount);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        allClients.remove(session);
        int onlineCount = WebScoketServer.onlineCount.decrementAndGet();
//        if (!isInit && onlineCount <= 0) {
//            executorService.shutdown();
//        }
        LOGGER.info("close webSocket, id:{}. onlineCount:{}", session.getId(), onlineCount);

    }

    /**
     * 连接失败调用的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("connect webSocket exception", error);
    }

    public void sendMessage(String message) throws IOException {
        for (Session client : allClients) {
            synchronized (this) {
                client.getBasicRemote().sendText(message);
            }
        }
    }

//    @Override
//    public void run() {
//        try {
//            System.out.println(onlineCount);
//            Response response = monitorService.getServers();
//            LOGGER.trace("execute getServers success, servers:{}", response);
//            this.sendMessage(JsonUtils.objectToJson(response));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public int getOnlineCount() {
        return onlineCount.get();
    }
}
