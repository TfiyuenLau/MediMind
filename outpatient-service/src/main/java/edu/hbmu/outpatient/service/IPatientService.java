package edu.hbmu.outpatient.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-10
 */
public interface IPatientService extends IService<Patient> {

    Patient getPatientById(Long id);

    IPage<Patient> getPatientByPage(Long page);

    List<Patient> getPatientList();

    List<Patient> getPatientByLikeName(String key);

    List<Patient> publishRegisterPatientList();

    Patient processPatientQueue(String queueName);

    int insertPatient(Patient patient);

    int updatePatient(Patient patient);

    int deletePatient(Long id);
}
