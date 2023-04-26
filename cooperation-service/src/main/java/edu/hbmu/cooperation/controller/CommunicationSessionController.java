package edu.hbmu.cooperation.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import edu.hbmu.cooperation.domain.request.CommunicationSessionParams;
import edu.hbmu.cooperation.domain.response.CommunicationSessionVO;
import edu.hbmu.cooperation.domain.response.ResultVO;
import edu.hbmu.cooperation.service.ICommunicationSessionService;
import edu.hbmu.cooperation.service.IGroupInfoService;
import edu.hbmu.fegin.client.OutpatientClient;
import edu.hbmu.fegin.domain.response.DoctorVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/communicationSession")
public class CommunicationSessionController {

    @Autowired
    private ICommunicationSessionService communicationSessionService;

    @Autowired
    private IGroupInfoService groupInfoService;

    @Autowired
    private OutpatientClient outpatientClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation("获取当前登陆者相关的所有会话列表")
    @SaCheckLogin
    @GetMapping("/getSessionByDoctorId")
    public ResultVO getSessionByDoctorId() {
        // 获取会话列表
        long id = StpUtil.getLoginIdAsLong();
        List<CommunicationSession> communicationSessionList = communicationSessionService.getSessionByDoctorId(id);
        if (communicationSessionList.size() == 0) {
            return ResultVO.errorMsg("暂无相关会话...");
        }

        // 封装视图
        List<CommunicationSessionVO> communicationSessionVOS = new ArrayList<>();
        for (CommunicationSession communicationSession : communicationSessionList) {
            CommunicationSessionVO communicationSessionVO = new CommunicationSessionVO();
            BeanUtils.copyProperties(communicationSession, communicationSessionVO);
            communicationSessionVO.setFromName(objectMapper
                    .convertValue(outpatientClient.getAccountById(communicationSession.getFromId()).getData(), DoctorVO.class)
                    .getDoctorName());
            // 封装toName字段
            if (communicationSession.getChatType() == 1) {// 会话为私信
                communicationSessionVO.setToName(objectMapper
                        .convertValue(outpatientClient.getAccountById(communicationSession.getToId()).getData(), DoctorVO.class)
                        .getDoctorName());
            } else if (communicationSession.getChatType() == 2) {// 会话为群聊
                communicationSessionVO.setToName(groupInfoService.getGroupInfoById(communicationSession.getToId()).getGroupName());
            }

            communicationSessionVOS.add(communicationSessionVO);
        }

        return ResultVO.ok(communicationSessionVOS);
    }

    @ApiOperation("添加一位好友（或团队群聊），这意味着为当前医生新增一条通讯会话")
    @SaCheckLogin
    @PostMapping("/insertCommunicationSession")
    public ResultVO insertCommunicationSession (@RequestBody CommunicationSessionParams communicationSessionParams) {
        // 判断communication_session表中是否包括了该会话与相反会话
        long fromId = StpUtil.getLoginIdAsLong();
        if (communicationSessionService.isSessionExist(fromId, communicationSessionParams.getToId(), communicationSessionParams.getChatType())) {
            return ResultVO.errorMsg("会话已存在");
        }

        // 封装CommunicationSession对象
        CommunicationSession communicationSession = new CommunicationSession();
        try {
            communicationSession.setFromId(fromId);// 发送人为当前的登录者
            communicationSession.setToId(communicationSessionParams.getToId());
            communicationSession.setLastMsg("暂无消息");
            communicationSession.setChatType(communicationSessionParams.getChatType());
            communicationSessionService.insertCommunicationSession(communicationSession);
        } catch (Exception e) {
            return ResultVO.errorMsg("添加失败！" + new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
