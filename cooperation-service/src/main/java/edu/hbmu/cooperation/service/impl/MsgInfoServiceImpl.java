package edu.hbmu.cooperation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import edu.hbmu.cooperation.domain.entity.MsgInfo;
import edu.hbmu.cooperation.dao.MsgInfoMapper;
import edu.hbmu.cooperation.service.ICommunicationSessionService;
import edu.hbmu.cooperation.service.IMsgInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
@Service
public class MsgInfoServiceImpl extends ServiceImpl<MsgInfoMapper, MsgInfo> implements IMsgInfoService {

    @Autowired
    private MsgInfoMapper msgInfoMapper;

    @Autowired
    private ICommunicationSessionService communicationSessionService;

    @Override
    public MsgInfo getMsgInfoById(Long id) {
        return msgInfoMapper.selectById(id);
    }

    /**
     * 通过会话外键uid字段获取私聊消息集合
     *
     * @param uid
     * @return
     */
    @Override
    public List<MsgInfo> getMsgInfoList(Long uid) {
        LambdaQueryWrapper<MsgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgInfo::getChatType, 1).eq(MsgInfo::getUid, uid);

        return msgInfoMapper.selectList(queryWrapper);
    }

    /**
     * 通过群组id获取群聊相关消息的集合
     *
     * @param uid
     * @return
     */
    @Override
    public List<MsgInfo> getGroupMsgInfoList(Long uid) {
        // 通过uid获取toId（即groupId），随后获取与groupId有关的所有会话
        Long groupId = communicationSessionService.getCommunicationById(uid).getToId();
        List<CommunicationSession> communicationSessionList = communicationSessionService.getSessionsByGroupId(groupId);
        // 封装与群有关的会话Id
        ArrayList<Long> sessionIds = new ArrayList<>();
        for (CommunicationSession communicationSession : communicationSessionList) {
            sessionIds.add(communicationSession.getId());
        }

        LambdaQueryWrapper<MsgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgInfo::getChatType, 2).in(MsgInfo::getUid, sessionIds);// 查找所有与会话id集合有关的MsgInfo

        return msgInfoMapper.selectList(queryWrapper);
    }

    /**
     * 通过会话id与登陆者id查询所有未读消息
     *
     * @param uid    会话id
     * @param fromId 发送者的ID
     * @return
     */
    @Override
    public List<MsgInfo> getUnreadMsgInfo(Long uid, Long fromId) {
        LambdaQueryWrapper<MsgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgInfo::getUid, uid)// 会话id
                .ne(MsgInfo::getFromId, fromId)// 只查询与发送者id不同的数据
                .eq(MsgInfo::getIsRead, 0);// 查询未读消息

        return msgInfoMapper.selectList(queryWrapper);
    }

    /**
     * 通过会话外键uid字段获取分页消息集合
     *
     * @param uid
     * @param page
     * @return
     */
    @Override
    public IPage<MsgInfo> getMsgInfoPage(Long page, Long uid) {
        Page<MsgInfo> msgInfoPage = new Page<>(page, 10);
        LambdaQueryWrapper<MsgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgInfo::getUid, uid);

        return msgInfoMapper.selectPage(msgInfoPage, queryWrapper);
    }

    /**
     * 插入一条消息
     *
     * @param msgInfo
     * @return
     */
    @Override
    public int insertMsg(MsgInfo msgInfo) {
        return msgInfoMapper.insert(msgInfo);
    }

    /**
     * 上传文件通用方法
     *
     * @param file     MultipartFile 文件对象
     * @param filePath 静态资源文件目录——img
     * @return 创建的targetPicFile对象
     * @throws FileNotFoundException 抛出找不到文件异常
     */
    @Override
    public File uploadingFile(MultipartFile file, String filePath) throws IOException {
        // 获取上传文件的地址
        String path = ResourceUtils.getURL("classpath:").getPath();
        String fileName = "img_msg_" + UUID.randomUUID() + ".jpeg";
        File uploadFile = new File(path + "/static/" + filePath + "/" + fileName);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        // 保存文件至target
        try {
            file.transferTo(uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 复制target文件到/static/#/(仅idea测试时使用)
        try {
            Files.copy(new File(uploadFile.getAbsolutePath()).toPath(),
                    new File(System.getProperty("user.dir") + "\\cooperation-service\\src\\main\\resources\\static\\" + filePath + "\\" + uploadFile.getName()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uploadFile;
    }

    /**
     * 更新该一个会话下的所有isRead状态
     *
     * @param msgInfo
     * @return
     */
    @Override
    public int updateMsgIsReadByUid(MsgInfo msgInfo) {
        LambdaUpdateWrapper<MsgInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MsgInfo::getUid, msgInfo.getUid()).set(MsgInfo::getIsRead, msgInfo.getIsRead());

        return msgInfoMapper.update(msgInfo, updateWrapper);
    }

}
