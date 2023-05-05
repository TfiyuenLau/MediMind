package edu.hbmu.cooperation.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import edu.hbmu.cooperation.domain.entity.MsgInfo;
import edu.hbmu.cooperation.domain.response.ResultVO;
import edu.hbmu.cooperation.service.ICommunicationSessionService;
import edu.hbmu.cooperation.service.IMsgInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/msgInfo")
public class MsgInfoController {

    @Autowired
    private IMsgInfoService msgInfoService;

    @Autowired
    private ICommunicationSessionService communicationSessionService;

    @ApiOperation("通过会话UID获取该会话的消息列表")
    @SaCheckLogin
    @GetMapping("/getMsgListByUid/{chatType}/{uid}")
    public ResultVO getMsgListByUid(@PathVariable("chatType") Integer chatType, @PathVariable("uid") Long uid) {
        // 获取对应的消息集合
        List<MsgInfo> msgInfoList = null;
        try {
            if (chatType == 1) {
                msgInfoList = msgInfoService.getMsgInfoList(uid);
            } else if (chatType == 2) {
                msgInfoList = msgInfoService.getGroupMsgInfoList(uid);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (msgInfoList == null || msgInfoList.size() == 0) {
            return ResultVO.ok(msgInfoList);
        }

        // 判断当前登录者是否与消息发送者id相同
        if (StpUtil.getLoginIdAsLong() != msgInfoList.get(msgInfoList.size() - 1).getFromId()) {
            // 重置会话的unread为0
            communicationSessionService.updateSessionUnread(uid, 0);

            // 重置所有该会话的消息的isRead字段为1
            MsgInfo msgInfo = new MsgInfo();
            msgInfo.setUid(uid);
            msgInfo.setIsRead(1);
            msgInfoService.updateMsgIsReadByUid(msgInfo);
        }
        return ResultVO.ok(msgInfoList);
    }

    @ApiOperation("查询当前登录医生的所有未读消息")
    @SaCheckLogin
    @GetMapping("/getUnreadMsgInfo")
    public ResultVO getUnreadMsgInfo() {
        // 获取所有会话相关并封装为未读消息
        long doctorId = StpUtil.getLoginIdAsLong();
        List<CommunicationSession> communicationSessionList = communicationSessionService.getSessionByDoctorId(doctorId);
        List<MsgInfo> msgInfos = new ArrayList<>();
        for (CommunicationSession communicationSession : communicationSessionList) {
            msgInfos.addAll(msgInfoService.getUnreadMsgInfo(communicationSession.getId(), doctorId));
        }

        return ResultVO.ok(msgInfos);
    }

    @ApiOperation("上传图片消息")
    @PostMapping("/uploadImg")
    public ResultVO uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadingFile = msgInfoService.uploadingFile(file, "img");

        return ResultVO.ok(uploadingFile.getName());
    }

}
