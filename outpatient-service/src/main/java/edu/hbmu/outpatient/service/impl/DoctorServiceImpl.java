package edu.hbmu.outpatient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hbmu.outpatient.domain.entity.Doctor;
import edu.hbmu.outpatient.dao.DoctorMapper;
import edu.hbmu.outpatient.service.IDoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public Doctor doLogin(String account, String password) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account" ,account);
        Doctor doctor = doctorMapper.selectOne(queryWrapper);

        if (doctor==null) {
            return null;
        }

        if (doctor.getPassword().equals(password)) {
            return doctor;
        } else {
            return null;
        }
    }

    @Override
    public Doctor getAccountInfo(Long id) {
        return doctorMapper.selectById(id);
    }

    @Override
    public String getAuthorize(Long id) {
        //根据ID查找用户权限
        Doctor doctor = doctorMapper.selectById(id);

        return doctor.getAuthority();
    }

    @Override
    public IPage<Doctor> getDoctorByPage(Long page) {

        return doctorMapper.selectPage(new Page<Doctor>(page, 20), null);
    }

    @Override
    public int insertDoctor(Doctor doctor) {
        return doctorMapper.insert(doctor);
    }

}
