package edu.hbmu.cooperation.websocket;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.cooperation.config.MessageEncoder;
import edu.hbmu.cooperation.config.WebSocketConfig;
import edu.hbmu.cooperation.domain.entity.GroupMember;
import edu.hbmu.cooperation.domain.entity.MsgInfo;
import edu.hbmu.cooperation.domain.request.MsgInfoParams;
import edu.hbmu.cooperation.service.ICommunicationSessionService;
import edu.hbmu.cooperation.service.IGroupMemberService;
import edu.hbmu.cooperation.service.IMsgInfoService;
import edu.hbmu.fegin.client.OutpatientClient;
import edu.hbmu.fegin.domain.response.DoctorVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint(value = "/chatWebSocket/{satoken}", configurator = WebSocketConfig.class, encoders = {MessageEncoder.class})
@Slf4j
public class ChatWebSocket {

    // 存放连接者的集合
    private static final CopyOnWriteArrayList<ChatWebSocket> chatWebSockets = new CopyOnWriteArrayList<>();

    //用来记录doctor_id和该session进行绑定
    private static final Map<Long, Session> sessionMap = new HashMap<>();

    // 实例化一个session会话
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    /**
     * 使用@Autowired自动注入发生错误；
     * 具体原理与解决请参考：
     * <a href="https://blog.csdn.net/u013905744/article/details/104818462">spring boot websocket @OnMessage中使用@Autowired spring bean报null错误</a>
     */
    private static ObjectMapper objectMapper;

    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        ChatWebSocket.objectMapper = objectMapper;
    }

    private static ICommunicationSessionService communicationSessionService;

    @Autowired
    private void setCommunicationSessionService(ICommunicationSessionService communicationSessionService) {
        ChatWebSocket.communicationSessionService = communicationSessionService;
    }

    private static IMsgInfoService msgInfoService;

    @Autowired
    private void setMsgInfoService(IMsgInfoService msgInfoService) {
        ChatWebSocket.msgInfoService = msgInfoService;
    }

    private static IGroupMemberService groupMemberService;

    @Autowired
    private void setGroupMemberService(IGroupMemberService groupMemberService) {
        ChatWebSocket.groupMemberService = groupMemberService;
    }

    private static OutpatientClient outpatientClient;

    @Autowired
    private void setOutpatientClient(OutpatientClient outpatientClient) {
        ChatWebSocket.outpatientClient = outpatientClient;
    }

    /**
     * 连接建立成功调用的方法，初始化session
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("satoken") String satoken) {
        // 加入连接List
        this.setSession(session);
        chatWebSockets.add(this);

        // 将登录者id与session映射
        Object loginIdByToken = StpUtil.getLoginIdByToken(satoken = satoken.split("=")[1]);
        if (loginIdByToken == null) {
            throw new SaTokenException("连接失败，无效Token：" + satoken);
        }
        sessionMap.put(Long.parseLong((String) loginIdByToken), this.getSession());
        log.info("【WebSocket消息】有新的连接，目前连接数:{}", chatWebSockets.size());
    }

    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose(Session session, @PathParam("satoken") String satoken) {
        // 移出set连接集合
        chatWebSockets.remove(this);

        // 移出map集合
        try {
            Object loginIdByToken = StpUtil.getLoginIdByToken(satoken = satoken.split("=")[1]);
            for (Long key : sessionMap.keySet()) {
                if (key.equals(Long.parseLong((String) loginIdByToken))) {
                    sessionMap.remove(key, sessionMap.get(key));
                }
            }
        } catch (SaTokenException e) {
            log.error(e.getMessage() + "###cause:" + e.getCause());
        }
        log.info("【websocket消息】连接断开，目前连接数:{}", chatWebSockets.size());
    }

    /**
     * 收到客户端消息后调用
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        try {
            // 解析json数据
            MsgInfoParams msgInfoParams = objectMapper.readValue(message, MsgInfoParams.class);

            // 封装对象
            MsgInfo msgInfo = new MsgInfo();
            BeanUtils.copyProperties(msgInfoParams, msgInfo);
            msgInfo.setFromName(objectMapper
                    .convertValue(outpatientClient.getAccountById(msgInfo.getFromId()).getData(), DoctorVO.class)
                    .getDoctorName());
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            msgInfo.setSendTime(LocalDateTime.parse(LocalDateTime.now().format(timeFormatter), timeFormatter));
            if (msgInfo.getChatType() == 1) {// 查找私聊与群聊id的方法逻辑不通
                msgInfo.setUid(communicationSessionService.getSessionIdByFromAndToId(msgInfo.getFromId(), msgInfo.getToId()));
            } else if (msgInfo.getChatType() == 2) {
                msgInfo.setUid(communicationSessionService.getGroupSessionIdByFromAndToId(msgInfo.getFromId(), msgInfo.getToId()));
            }

            // 根据聊天类型确认推送目标：会话类型——(群组消息/个人聊天/系统消息:2/1/0)
            Session fromSession = sessionMap.get(msgInfo.getFromId());
            fromSession.getAsyncRemote().sendObject(msgInfo);// 发送给发送者
            Integer chatType = msgInfo.getChatType();
            if (chatType == 1) {// 私信聊天
                Session toSession = sessionMap.get(msgInfo.getToId());
                if (toSession != null) {// 用户在线
                    // 发送给接收者
                    toSession.getAsyncRemote().sendObject(msgInfo);
                } else {// 用户不在线，更新未读消息
                    communicationSessionService.updateSessionUnread(msgInfo.getUid(),
                            communicationSessionService.getCommunicationById(msgInfo.getUid()).getUnreadCount() + 1);
                }
            } else if (chatType == 2) {// 群聊
                // 获取所有在群里的成员
                List<GroupMember> groupMembers = groupMemberService.getGroupMembersByGroupInfoId(msgInfo.getFromId());
                for (GroupMember groupMember : groupMembers) {
                    Session toSession = sessionMap.get(groupMember.getGroupMember());
                    if (toSession != null) {
                        toSession.getAsyncRemote().sendObject(msgInfo);
                    } else {
                        continue;
                    }
                }
            } else {// 系统消息
                broadcast(msgInfo.getMsg());
            }

            try {
                // 保存消息至数据库
                msgInfoService.insertMsg(msgInfo);

                // 更新会话表
                if (chatType == 1) {
                    communicationSessionService.updateSessionLastMsg(msgInfo.getUid(), msgInfo.getMsg(), msgInfo.getFromName());
                } else if (chatType == 2) {
                    communicationSessionService.updateGroupSessionLastMsg(msgInfo.getToId(), msgInfo.getMsg(), msgInfo.getFromName());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            log.info("【websocket消息】收到客户端发来的消息:{}", msgInfo.getMsg());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向指定客户端推送消息
     *
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        try {
            log.info("向id为：" + session.getId() + "，发送：" + message);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向指定用户推送消息
     *
     * @param doctorId
     * @param message
     */
    public static void sendMessage(long doctorId, String message) {
        Session session = sessionMap.get(doctorId);
        if (session != null) {
            sendMessage(session, message);
        }
    }

    /**
     * 广播机制
     *
     * @param msg
     */
    private static void broadcast(String msg) {
        for (ChatWebSocket client : chatWebSockets) {
            try {
                synchronized (client) {
                    sendMessage(client.session, msg);
                }
            } catch (Exception e) {
                System.out.println("Chat Error: Failed to send message to client");
                chatWebSockets.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String message = String.format("* %s %s", client.session.getId(), "has been disconnected.");
                broadcast(message);
            }
        }
    }

}
