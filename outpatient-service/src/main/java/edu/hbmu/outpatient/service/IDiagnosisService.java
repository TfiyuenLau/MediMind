package edu.hbmu.outpatient.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Diagnosis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
public interface IDiagnosisService extends IService<Diagnosis> {

    IPage<Diagnosis> getDiagnosisByPage(Long page);

    IPage<Diagnosis> getDiagnosisByDoctor(Long id, Long page);

    IPage<Diagnosis> getDiagnosisByPatientId(Long patientId, Long page);

    int insertDiagnosis(Diagnosis diagnosis);
}
