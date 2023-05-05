package edu.hbmu.cooperation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import edu.hbmu.cooperation.dao.CommunicationSessionMapper;
import edu.hbmu.cooperation.domain.entity.GroupMember;
import edu.hbmu.cooperation.service.ICommunicationSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hbmu.cooperation.service.IGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
@Service
public class CommunicationSessionServiceImpl extends ServiceImpl<CommunicationSessionMapper, CommunicationSession> implements ICommunicationSessionService {

    @Autowired
    private CommunicationSessionMapper communicationSessionMapper;

    @Autowired
    private IGroupMemberService groupMemberService;

    @Override
    public CommunicationSession getCommunicationById(Long id) {
        return communicationSessionMapper.selectById(id);
    }

    /**
     * 通过doctorId获取与其有关的会话列表
     *
     * @param doctorId
     * @return
     */
    @Override
    public List<CommunicationSession> getSessionByDoctorId(Long doctorId) {
        LambdaQueryWrapper<CommunicationSession> queryWrapper = new LambdaQueryWrapper<>();
        // 查找相关的私聊会话
        queryWrapper.eq(CommunicationSession::getChatType, 1).eq(CommunicationSession::getFromId, doctorId)
                .or().eq(CommunicationSession::getChatType, 1).eq(CommunicationSession::getToId, doctorId);
        // 相关群聊对话
        List<Long> groupIds = new ArrayList<>();// 封装相关群聊的groupId集合
        for (GroupMember groupMember : groupMemberService.getGroupMembersByMemberId(doctorId)) {
            groupIds.add(groupMember.getGroupId());
        }
        queryWrapper.or().eq(CommunicationSession::getChatType, 2)
                .in(CommunicationSession::getToId, groupIds).eq(CommunicationSession::getFromId, doctorId);

        return communicationSessionMapper.selectList(queryWrapper);
    }

    /**
     * 通过fromId toId查找私聊会话id
     *
     * @param fromId
     * @param toId
     * @return
     */
    @Override
    public Long getSessionIdByFromAndToId(Long fromId, Long toId) {
        LambdaQueryWrapper<CommunicationSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommunicationSession::getChatType, 1).eq(CommunicationSession::getFromId, fromId).eq(CommunicationSession::getToId, toId)
                .or().eq(CommunicationSession::getChatType, 1).eq(CommunicationSession::getFromId, toId).eq(CommunicationSession::getToId, fromId);

        CommunicationSession communicationSession = communicationSessionMapper.selectOne(queryWrapper);
        if (communicationSession == null) {
            return -1L;
        }
        return communicationSession.getId();
    }

    /**
     * 通过fromId toId查找群聊会话id
     *
     * @param fromId
     * @param toId
     * @return
     */
    @Override
    public Long getGroupSessionIdByFromAndToId(Long fromId, Long toId) {
        LambdaQueryWrapper<CommunicationSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommunicationSession::getChatType, 2)
                .eq(CommunicationSession::getFromId, fromId).eq(CommunicationSession::getToId, toId);

        CommunicationSession communicationSession = communicationSessionMapper.selectOne(queryWrapper);
        if (communicationSession == null) {
            return -1L;
        }
        return communicationSession.getId();
    }

    /**
     * 通过groupId查找所有相关的群消息会话
     *
     * @param groupId
     * @return
     */
    @Override
    public List<CommunicationSession> getSessionsByGroupId(Long groupId) {
        LambdaQueryWrapper<CommunicationSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommunicationSession::getChatType, 2).eq(CommunicationSession::getToId, groupId);

        return communicationSessionMapper.selectList(queryWrapper);
    }

    /**
     * 插入一条会话
     *
     * @param communicationSession CommunicationSession对象
     * @return int
     */
    @Override
    public int insertCommunicationSession (CommunicationSession communicationSession) {

        return communicationSessionMapper.insert(communicationSession);
    }

    /**
     * 判断会话是否已经存在
     *
     * @param fromId
     * @param toId
     * @param chatType
     * @return
     */
    @Override
    public boolean isSessionExist(Long fromId, Long toId, Integer chatType) {
        LambdaQueryWrapper<CommunicationSession> queryWrapper = new LambdaQueryWrapper<>();
        // 构造条件：fromId、toId与其相反是否存在且chatType相同
        queryWrapper.eq(CommunicationSession::getFromId, fromId).eq(CommunicationSession::getToId, toId).eq(CommunicationSession::getChatType, chatType)
                .or().eq(CommunicationSession::getFromId, toId).eq(CommunicationSession::getToId, fromId).eq(CommunicationSession::getChatType, chatType);
        CommunicationSession communicationSession = communicationSessionMapper.selectOne(queryWrapper);
        return communicationSession != null;
    }

    @Override
    public int updateSession(CommunicationSession communicationSession) {
        return communicationSessionMapper.update(communicationSession, null);
    }

    /**
     * 使用新消息更新会话中的最后一条消息字段
     *
     * @param id 更新的会话id
     * @param message 新消息
     * @param doctorName 医生姓名
     * @return flag
     */
    @Override
    public int updateSessionLastMsg(Long id, String message, String doctorName) {
        CommunicationSession communicationSession = new CommunicationSession();
        communicationSession.setLastMsg(message);
        communicationSession.setLastDoctorName(doctorName);

        LambdaUpdateWrapper<CommunicationSession> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CommunicationSession::getId, id)
                .set(CommunicationSession::getLastMsg, message)// 更新最后一条消息
                .set(CommunicationSession::getLastDoctorName, doctorName)// 更新消息发送者
                .set(CommunicationSession::getLastTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

        return communicationSessionMapper.update(communicationSession, updateWrapper);
    }

    /**
     * 更新所有群消息会话中的最后一条消息
     *
     * @param groupId 更新的会话id
     * @param message 新消息
     * @param doctorName 医生姓名
     * @return flag
     */
    @Override
    public int updateGroupSessionLastMsg(Long groupId, String message, String doctorName) {
        int count = 0;// 计数修改会话
        // 查询所有需要更新的群会话
        List<CommunicationSession> communicationSessionList = this.getSessionsByGroupId(groupId);
        for (CommunicationSession communicationSession : communicationSessionList) {// 批量更新
            LambdaUpdateWrapper<CommunicationSession> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(CommunicationSession::getId, communicationSession.getId())
                    .set(CommunicationSession::getLastMsg, message)
                    .set(CommunicationSession::getLastDoctorName, doctorName)
                    .set(CommunicationSession::getLastTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

            count += communicationSessionMapper.update(communicationSession, updateWrapper);
        }

        return count;
    }

    /**
     * 更新未读消息个数
     *
     * @param id
     * @param unreadCount
     * @return
     */
    @Override
    public int updateSessionUnread(Long id, Integer unreadCount) {
        CommunicationSession communicationSession = new CommunicationSession();

        LambdaUpdateWrapper<CommunicationSession> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CommunicationSession::getId, id)
                .set(CommunicationSession::getUnreadCount, unreadCount);

        return communicationSessionMapper.update(communicationSession, updateWrapper);
    }

    public int updateGroupSessionsUnread() {
        // TODO：更新群组的未读消息；需要根据具体的群聊会话更新其的单独的未读消息

        return -1;
    }

}
