package edu.hbmu.cooperation.service;

import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
public interface ICommunicationSessionService extends IService<CommunicationSession> {

    CommunicationSession getCommunicationById(Long id);

    List<CommunicationSession> getSessionByDoctorId(Long doctorId);

    Long getSessionIdByFromAndToId(Long fromId, Long toId);

    Long getGroupSessionIdByFromAndToId(Long fromId, Long toId);

    List<CommunicationSession> getSessionByGroupId(Long groupId);

    int insertCommunicationSession(CommunicationSession communicationSession);

    boolean isSessionExist(Long formId, Long toId, Integer chatType);

    int updateSession(CommunicationSession communicationSession);

    int updateSessionLastMsg(Long uid, String message, String doctorName);

    int updateGroupSessionLastMsg(Long groupId, String message, String doctorName);

    int updateSessionUnread(Long id, Integer unreadCount);
}
