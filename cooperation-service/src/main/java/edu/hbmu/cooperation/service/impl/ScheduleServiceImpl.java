package edu.hbmu.cooperation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import edu.hbmu.cooperation.domain.entity.Schedule;
import edu.hbmu.cooperation.dao.ScheduleMapper;
import edu.hbmu.cooperation.service.IScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-23
 */
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> getScheduleList() {
        return scheduleMapper.selectList(null);
    }

    @Override
    public List<Schedule> getScheduleByDoctorId(Long doctorId) {
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDoctorId, doctorId);
        return scheduleMapper.selectList(queryWrapper);
    }

    /**
     * 获取医生Id对应的剩余 remindedDay 天过期的日程集合
     *
     * @param doctorId
     * @param remindedDay
     * @return
     */
    @Override
    public List<Schedule> getRemindScheduleByDoctorId(Long doctorId, Integer remindedDay) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime remindTime = now.plusDays(remindedDay);// 设置remindTime为任务结束时间前 remindedDay 天

        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDoctorId, doctorId).eq(Schedule::getStatus, 0)// 未完成的日程
                .ge(Schedule::getEndTime, now).le(Schedule::getEndTime, remindTime);// 日期在now和now+remindedDay之间

        return scheduleMapper.selectList(queryWrapper);
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        return scheduleMapper.insert(schedule);
    }

    @Override
    public int updateScheduleStatus(Schedule schedule) {
        LambdaUpdateWrapper<Schedule> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Schedule::getScheduleId, schedule.getScheduleId())
                .set(Schedule::getUrgency, schedule.getUrgency())
                .set(Schedule::getStatus, schedule.getStatus());

        return scheduleMapper.update(schedule, updateWrapper);
    }

    @Override
    public int updateSchedule(Schedule schedule) {
        return scheduleMapper.updateById(schedule);
    }

}
