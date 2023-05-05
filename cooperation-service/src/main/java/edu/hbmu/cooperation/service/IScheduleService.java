package edu.hbmu.cooperation.service;

import edu.hbmu.cooperation.domain.entity.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-23
 */
public interface IScheduleService extends IService<Schedule> {

    List<Schedule> getScheduleByDoctorId(Long id);

    List<Schedule> getScheduleList();

    List<Schedule> getRemindScheduleByDoctorId(Long doctorId, Integer remindedDay);

    int insertSchedule(Schedule schedule);

    int updateScheduleStatus(Schedule schedule);

    int updateSchedule(Schedule schedule);
}
