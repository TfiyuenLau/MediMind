package edu.hbmu.cooperation.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.cooperation.domain.entity.Schedule;
import edu.hbmu.cooperation.domain.request.ScheduleParams;
import edu.hbmu.cooperation.domain.response.ResultVO;
import edu.hbmu.cooperation.domain.response.ScheduleVO;
import edu.hbmu.cooperation.service.IScheduleService;
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
 * 前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-23
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    @Autowired
    private OutpatientClient outpatientClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation("获取当前登录医生的所有日程安排计划")
    @GetMapping("/getScheduleByDoctorId")
    public ResultVO getScheduleByDoctor() {
        // 获取当前登录的医生id
        long doctorId = StpUtil.getLoginIdAsLong();
        List<Schedule> scheduleList = scheduleService.getScheduleByDoctorId(doctorId);// 查询相关日程

        // 封装视图对象
        List<ScheduleVO> scheduleVOS = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleVO scheduleVO = new ScheduleVO();
            BeanUtils.copyProperties(schedule, scheduleVO);
            scheduleVO.setDoctor(objectMapper.convertValue(outpatientClient.getAccountById(doctorId).getData(), DoctorVO.class));
            scheduleVOS.add(scheduleVO);
        }

        return ResultVO.ok(scheduleVOS);
    }

    @ApiOperation("通过id获取医生的所有日程安排计划")
    @GetMapping("/getScheduleByDoctorId/{doctorId}")
    public ResultVO getScheduleByDoctorId(@PathVariable("doctorId") Long doctorId) {
        List<Schedule> scheduleList = scheduleService.getScheduleByDoctorId(doctorId);

        List<ScheduleVO> scheduleVOS = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleVO scheduleVO = new ScheduleVO();
            BeanUtils.copyProperties(schedule, scheduleVO);
            scheduleVO.setDoctor(objectMapper.convertValue(outpatientClient.getAccountById(doctorId).getData(), DoctorVO.class));
            scheduleVOS.add(scheduleVO);
        }

        return ResultVO.ok(scheduleVOS);
    }

    @ApiOperation("获取所有日程安排")
    @GetMapping("/getScheduleList")
    public ResultVO getScheduleList() {
        List<Schedule> scheduleList = scheduleService.getScheduleList();

        List<ScheduleVO> scheduleVOS = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleVO scheduleVO = new ScheduleVO();
            BeanUtils.copyProperties(schedule, scheduleVO);
            scheduleVO.setDoctor(objectMapper.convertValue(outpatientClient.getAccountById(schedule.getDoctorId()).getData(), DoctorVO.class));
            scheduleVOS.add(scheduleVO);
        }

        return ResultVO.ok(scheduleVOS);
    }

    @ApiOperation("获取医生Id对应的还剩remindedDay过期的日程安排")
    @GetMapping("/getRemindSchedule/{doctorId}/{remindedDay}")
    public ResultVO getRemindSchedule(@PathVariable("doctorId") Long doctorId, @PathVariable("remindedDay") Integer remindedDay) {
        List<Schedule> remindScheduleList = scheduleService.getRemindScheduleByDoctorId(doctorId, remindedDay);

        return ResultVO.ok(remindScheduleList);
    }

    @ApiOperation("新增一条日程安排（doctorId仅需传null），时间格式：yyyy-MM-dd HH:mm:ss（字段为空则默认当前时间）")
    @PostMapping("/insertSchedule")
    public ResultVO insertSchedule(@RequestBody ScheduleParams scheduleParams) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleParams, schedule);
        long doctorId = StpUtil.getLoginIdAsLong();
        schedule.setDoctorId(doctorId);
        if (scheduleService.insertSchedule(schedule) < 1) {
            return ResultVO.errorMsg("插入失败");
        }

        return ResultVO.ok();
    }

    @ApiOperation("向一名医生指派日程安排，时间格式同上")
    @PostMapping("/insertScheduleToDoctor")
    public ResultVO insertScheduleToDoctor(@RequestBody ScheduleParams scheduleParams) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleParams, schedule);
        if (scheduleService.insertSchedule(schedule) < 1) {
            return ResultVO.errorMsg("插入失败");
        }

        return ResultVO.ok();
    }

    @ApiOperation("标记该日程已完成")
    @PutMapping("/updateScheduleStatus")
    public ResultVO updateScheduleStatus(@RequestParam Long scheduleId) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setStatus(Byte.parseByte("1"));// 标记已完成
        schedule.setUrgency(-1);// 将优先级置为-1
        try {
            scheduleService.updateScheduleStatus(schedule);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("更新该日程的信息")
    @PutMapping("/updateSchedule")
    public ResultVO updateSchedule(@RequestBody ScheduleParams scheduleParams) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleParams, schedule);
        try {
            scheduleService.updateSchedule(schedule);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
