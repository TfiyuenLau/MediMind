package edu.hbmu.cooperation.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import edu.hbmu.cooperation.domain.entity.MsgInfo;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * WebSocket服务解码器：sendObject()
 */
public class MessageEncoder implements Encoder.Text<MsgInfo> {

    @Override
    public void destroy() {

    }

    @Override
    public void init(EndpointConfig arg0) {

    }

    @Override
    public String encode(MsgInfo msgInfo) throws EncodeException {
        try {
            return new JsonMapper().writeValueAsString(msgInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
