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

    int insertSchedule(Schedule schedule);
}
