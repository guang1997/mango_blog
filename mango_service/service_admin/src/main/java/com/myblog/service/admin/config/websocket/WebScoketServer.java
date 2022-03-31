package com.myblog.service.admin.config.websocket;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Component
@ServerEndpoint("/admin/websocket")
public class WebScoketServer {

    private static Logger LOGGER = LoggerFactory.getLogger(WebScoketServer.class);

    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 用来存放每个客户端对应的链接对象。
     */
    private static CopyOnWriteArraySet<Session> allClients = new CopyOnWriteArraySet<>();

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        allClients.add(session);
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

    public int getOnlineCount() {
        return onlineCount.get();
    }
}
