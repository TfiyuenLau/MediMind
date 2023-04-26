package edu.hbmu.cooperation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
@Slf4j
public class WebSocketConfig extends ServerEndpointConfig.Configurator{

    @Bean
    public ServerEndpointExporter serverEndpointConfig() {
        return new ServerEndpointExporter();
    }

}
