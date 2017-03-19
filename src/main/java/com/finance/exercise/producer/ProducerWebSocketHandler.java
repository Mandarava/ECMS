package com.finance.exercise.producer;

import com.google.gson.Gson;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

/**
 * Created by zt on 2017/3/18.
 */
public class ProducerWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProducerWebSocketHandler.class);

    private static CircleQueue circleQueue = new CircleQueue(20);

    private static List<WebSocketSession> sessions = new ArrayList<>();

    @PostConstruct
    private void producing() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                int times = new Random().nextInt(5) + 1;
                for (int i = 0; i < times; i++) {
                    int number = new Random().nextInt(1000);
                    circleQueue.add(number);
                    sendMessage();
                }
            }
        }, 0, 5 * 10);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("connect to the websocket success......");
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            sessions.remove(session);
            session.close();
        }
        logger.debug("websocket connection closed......");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("websocket connection closed......");
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void sendMessage() {
        try {
            if (CollectionUtils.isNotEmpty(sessions)) {
                for (WebSocketSession session : sessions) {
                    session.sendMessage(new TextMessage(new Gson().toJson(circleQueue.getCircle())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
