package edu.hbmu.outpatient.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Doctor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
public interface IDoctorService extends IService<Doctor> {

    public Doctor doLogin(String account, String password);

    Doctor getAccountInfo(Long id);

    String getAuthorize(Long id);

    IPage<Doctor> getDoctorByPage(Long page);

    int insertDoctor(Doctor doctor);

    int updateDoctorById(Doctor doctor);

    Doctor doRealPassword(Long doctorId, String password);

    int updateDoctorPassword(Doctor doctor);

    int updateDoctorAuthority(Doctor doctor);
}
