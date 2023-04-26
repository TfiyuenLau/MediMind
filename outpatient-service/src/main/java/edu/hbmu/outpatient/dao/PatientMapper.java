package edu.hbmu.outpatient.dao;

import edu.hbmu.outpatient.domain.entity.Patient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-10
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {

}
