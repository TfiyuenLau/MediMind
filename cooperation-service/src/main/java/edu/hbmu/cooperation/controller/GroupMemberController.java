package edu.hbmu.cooperation.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import edu.hbmu.cooperation.domain.entity.GroupMember;
import edu.hbmu.cooperation.domain.response.GroupMemberVo;
import edu.hbmu.cooperation.domain.response.ResultVO;
import edu.hbmu.cooperation.service.ICommunicationSessionService;
import edu.hbmu.cooperation.service.IGroupMemberService;
import edu.hbmu.fegin.client.OutpatientClient;
import edu.hbmu.fegin.domain.entity.Department;
import edu.hbmu.fegin.domain.response.DoctorVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/groupMember")
public class GroupMemberController {

    @Autowired
    private IGroupMemberService groupMemberService;

    @Autowired
    private ICommunicationSessionService communicationSessionService;

    @Autowired
    private OutpatientClient outpatientClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation("获取团队成员视图对象集合")
    @GetMapping("/getGroupMembersByGroupInfoId/{group_id}")
    public ResultVO getGroupMembersByGroupInfoId(@PathVariable("group_id") Long groupId) {
        // 获取GroupMemberList数据库映射对象集合
        List<GroupMember> groupMemberList = groupMemberService.getGroupMembersByGroupInfoId(groupId);

        // 封装前端视图对象列表
        ArrayList<GroupMemberVo> groupMemberVos = new ArrayList<>();
        for (GroupMember groupMember : groupMemberList) {
            try {
                // 封装GroupMemberVo
                GroupMemberVo groupMemberVo = new GroupMemberVo();
                DoctorVO doctor = objectMapper.convertValue(outpatientClient.getAccountById(groupMember.getGroupMember()).getData(), DoctorVO.class);
                BeanUtils.copyProperties(doctor, groupMemberVo);
                groupMemberVo.setGroupId(groupId);
                groupMemberVo.setDepartment(objectMapper.convertValue(outpatientClient.getDepartmentById(groupMember.getGroupMember()).getData(), Department.class).getDepartmentName());

                groupMemberVos.add(groupMemberVo);
            } catch (Exception e) {
                return ResultVO.errorMsg(new RuntimeException(e).getMessage());
            }
        }

        return ResultVO.ok(groupMemberVos);
    }

    @ApiOperation("邀请医生进入该团队")
    @PostMapping("/inviteGroupMember")
    public ResultVO inviteGroupMember(Long groupId, Long memberId) {
        // 添加群成员
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setGroupMember(memberId);
        if (groupMemberService.insertGroupMember(groupMember) < 1) {
            return ResultVO.errorMsg("未邀请成功");
        }

        // 新建相关群会话
        CommunicationSession communicationSession = new CommunicationSession();
        communicationSession.setChatType(2);
        communicationSession.setFromId(memberId);
        communicationSession.setToId(groupId);
        communicationSession.setLastMsg("暂无消息");
        communicationSessionService.insertCommunicationSession(communicationSession);

        return ResultVO.ok();
    }

    @ApiOperation("申请进入该团队")
    @PostMapping("/applyGroupMember")
    public ResultVO applyGroupMember(Long groupId) {
        long memberId = StpUtil.getLoginIdAsLong();
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setGroupMember(memberId);

        if (groupMemberService.insertGroupMember(groupMember) < 1) {
            return ResultVO.errorMsg("未申请成功");
        }
        return ResultVO.ok();
    }

}
