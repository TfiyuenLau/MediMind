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

    @ApiOperation("创建团队")
    @PostMapping("/insertGroupInfo")
    public ResultVO insertGroupInfo(@RequestBody GroupInfoParams groupInfoParams) {
        GroupInfo groupInfo = new GroupInfo();// 封装GroupInfo数据库映射对象
        BeanUtils.copyProperties(groupInfoParams, groupInfo);
        groupInfo.setGroupCreateUid(StpUtil.getLoginIdAsLong());

        if (groupInfoService.insertGroupInfo(groupInfo) < 1) {
            return ResultVO.errorMsg("插入失败");
        }

        return ResultVO.ok();
    }

}
