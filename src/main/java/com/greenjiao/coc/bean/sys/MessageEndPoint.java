package com.greenjiao.coc.bean.sys;

import com.alibaba.fastjson2.JSON;
import com.greenjiao.coc.config.websocket.GetHttpSessionConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Yan
 * @Date 2023/06/11 7:59
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfig.class)
@Component
public class MessageEndPoint {
    private static final Map<String, Session> webSocketSet = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    /**
     * 建立websocket连接后，被调用
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // session保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String name = session.getUserPrincipal().getName();
        webSocketSet.put(name, session);
        // 广播消息
        broadcastAll(name + "上线了");
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        Message message = JSON.parseObject(msg, Message.class);
        sendMsg(message);
        System.out.println("某人发送了消息");
    }

    @OnClose
    public void onClose(Session session) {
        String name = session.getUserPrincipal().getName();
        webSocketSet.remove(name);
        System.out.println("某人关闭连接");
    }

    public void onError(Session session, Throwable error) {
        String name = session.getUserPrincipal().getName();
        log.error("发生错误,用户：{}，发生错误", name);
        error.printStackTrace();
    }

    private void broadcastAll(String message) {
        Set<Map.Entry<String, Session>> entries = webSocketSet.entrySet();
        try {
            for (Map.Entry<String, Session> entry : entries) {
                Session session = entry.getValue();
                // 发送消息
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message message) {
        Session session = webSocketSet.get(message.getTo());
        String jsonString = JSON.toJSONString(message);
        try {
            session.getBasicRemote().sendText(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
