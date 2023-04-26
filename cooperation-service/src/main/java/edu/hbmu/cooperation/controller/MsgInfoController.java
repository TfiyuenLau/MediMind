package edu.hbmu.cooperation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

        // 当点击该uid对应的会话时重置会话的unread count
        communicationSessionService.updateSessionUnread(uid, 0);

        return ResultVO.ok(msgInfoList);
    }

    @ApiIgnore
    @ApiOperation("通过会话UID获取消息分页列表")
    @GetMapping("/getMsgPageByUid/{page}/{uid}")
    public ResultVO getMsgPageByUid(@PathVariable("page") Long page, @PathVariable("uid") Long uid) {
        IPage<MsgInfo> msgInfoIPage = msgInfoService.getMsgInfoPage(page, uid);
        if (msgInfoIPage.getRecords().size() == 0) {
            return ResultVO.errorMsg("没有相关消息");
        }

        return ResultVO.ok(msgInfoIPage);
    }

    @ApiOperation("上传图片消息")
    @PostMapping("/uploadImg")
    public ResultVO uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadingFile = msgInfoService.uploadingFile(file, "img");

        return ResultVO.ok(uploadingFile.getName());
    }

}
