package edu.hbmu.cooperation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpointConfig;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends ServerEndpointConfig.Configurator implements WebSocketMessageBrokerConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointConfig() {

        return new ServerEndpointExporter();
    }

    /**
     * 注册STOMP端点以接收客户端的连接
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 配置客户端尝试连接地址
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 设置广播节点
        registry.enableSimpleBroker("/topic", "/user");
        // 客户端向服务端发送消息需有/app 前缀
//        registry.setApplicationDestinationPrefixes("/app");
        // 指定用户发送（一对一）的前缀 /doctor/
        registry.setUserDestinationPrefix("/doctor/");
    }

}
