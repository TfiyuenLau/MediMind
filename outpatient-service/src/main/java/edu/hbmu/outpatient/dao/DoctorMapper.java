package edu.hbmu.outpatient.dao;

import edu.hbmu.outpatient.domain.entity.Doctor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

}
