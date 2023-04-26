package edu.hbmu.cooperation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hbmu.cooperation.domain.entity.Schedule;
import edu.hbmu.cooperation.dao.ScheduleMapper;
import edu.hbmu.cooperation.service.IScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Schedule> getScheduleByDoctorId(Long doctorId) {
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDoctorId, doctorId);
        return scheduleMapper.selectList(queryWrapper);
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        return scheduleMapper.insert(schedule);
    }

}
