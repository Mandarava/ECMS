package com.finance.exercise.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by zt on 2017/2/4.
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketProducer extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Bean
    public WebSocketHandler producerWebSocketHandler() {
        return new ProducerWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(producerWebSocketHandler(), "/producer-websocket");
        registry.addHandler(producerWebSocketHandler(), "/sockjs/producer-websocket")
                .withSockJS();
    }
}
