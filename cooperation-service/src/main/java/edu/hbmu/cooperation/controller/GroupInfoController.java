package edu.hbmu.cooperation.controller;

import cn.dev33.satoken.stp.StpUtil;
import edu.hbmu.cooperation.domain.entity.GroupInfo;
import edu.hbmu.cooperation.domain.request.GroupInfoParams;
import edu.hbmu.cooperation.domain.response.ResultVO;
import edu.hbmu.cooperation.service.IGroupInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/groupInfo")
public class GroupInfoController {

    @Autowired
    private IGroupInfoService groupInfoService;

    @ApiOperation("通过id查询一条GroupInfo")
    @GetMapping("/getGroupInfoById/{id}")
    public ResultVO getGroupInfoById(@PathVariable("id") Long id) {
        GroupInfo groupInfo = groupInfoService.getGroupInfoById(id);

        return ResultVO.ok(groupInfo);
    }

    @ApiOperation("获取全部团队信息集合")
    @GetMapping("/getGroupInfoList")
    public ResultVO getGroupInfoList() {
        List<GroupInfo> groupInfoList = groupInfoService.getGroupInfoList();

        return ResultVO.ok(groupInfoList);
    }

    @ApiOperation("模糊查询团队名称获取团队列表")
    @GetMapping("/getGroupInfoByName/{name}")
    public ResultVO getGroupInfoByName(@PathVariable("name") String name) {
        List<GroupInfo> groupInfoList = null;
        try {
            groupInfoList = groupInfoService.getGroupByName(name);
        } catch (Exception e) {
            return ResultVO.errorException(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok(groupInfoList);
    }

    @ApiOperation("创建医生团队，id传输null")
    @PostMapping("/insertGroupInfo")
    public ResultVO insertGroupInfo(@RequestBody GroupInfoParams groupInfoParams) {
        GroupInfo groupInfo = new GroupInfo();// 封装GroupInfo数据库映射对象
        BeanUtils.copyProperties(groupInfoParams, groupInfo);
        groupInfo.setGroupCreateUid(StpUtil.getLoginIdAsLong());
        try {
            groupInfoService.insertGroupInfo(groupInfo);
        } catch (Exception e) {
            return ResultVO.errorMsg("插入失败" + new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("更新指定id的团队信息")
    @PutMapping("/updateGroupInfo")
    public ResultVO updateGroupInfo(@RequestBody GroupInfoParams groupInfoParams) {
        GroupInfo groupInfo = new GroupInfo();
        BeanUtils.copyProperties(groupInfoParams, groupInfo);
        try {
            groupInfoService.updateGroupInfo(groupInfo);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
