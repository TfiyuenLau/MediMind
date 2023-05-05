package edu.hbmu.outpatient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hbmu.outpatient.domain.entity.Diagnosis;
import edu.hbmu.outpatient.dao.DiagnosisMapper;
import edu.hbmu.outpatient.service.IDiagnosisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hbmu.outpatient.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Service
public class DiagnosisServiceImpl extends ServiceImpl<DiagnosisMapper, Diagnosis> implements IDiagnosisService {

    @Autowired
    private DiagnosisMapper diagnosisMapper;

    @Autowired
    private IDoctorService doctorService;

    @Override
    public IPage<Diagnosis> getDiagnosisByPage(Long page) {
        return diagnosisMapper.selectPage(new Page<>(page, 20), null);
    }

    @Override
    public IPage<Diagnosis> getDiagnosisByDoctor(Long id, Long page) {
        String doctorName = doctorService.getAccountInfo(id).getDoctorName();
        LambdaQueryWrapper<Diagnosis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Diagnosis::getAttendingDoctor, doctorName);

        return diagnosisMapper.selectPage(new Page<>(page, 20), queryWrapper);
    }

    @Override
    public List<Diagnosis> getDiagnosisByPatientId(Long patientId) {
        LambdaQueryWrapper<Diagnosis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Diagnosis::getPatientId, patientId);

        return diagnosisMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Diagnosis> getDiagnosisByPatientId(Long patientId, Long page) {
        LambdaQueryWrapper<Diagnosis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Diagnosis::getPatientId, patientId);

        return diagnosisMapper.selectPage(new Page<>(page, 20), queryWrapper);
    }

    @Override
    public int insertDiagnosis(Diagnosis diagnosis) {
        return diagnosisMapper.insert(diagnosis);
    }

}
