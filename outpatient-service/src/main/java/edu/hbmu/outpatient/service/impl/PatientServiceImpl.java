package edu.hbmu.outpatient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hbmu.outpatient.domain.entity.Patient;
import edu.hbmu.outpatient.dao.PatientMapper;
import edu.hbmu.outpatient.service.IPatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-10
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Patient getPatientById(Long id) {
        return patientMapper.selectById(id);
    }

    @Override
    public IPage<Patient> getPatientByPage(Long page) {
        return patientMapper.selectPage(new Page<>(page, 25), null);
    }

    @Override
    public List<Patient> getPatientList() {
        return patientMapper.selectList(null);
    }

    @Override
    public List<Patient> getPatientByLikeName(String key) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Patient::getPatientName, key).or().like(Patient::getPatientGender, key);// 从姓名和性别中模糊查询

        return patientMapper.selectList(queryWrapper);
    }

    /**
     * 生成随机挂号病人信息
     *
     * @return
     */
    @Override
    public List<Patient> publishRegisterPatientList() {
        Random random = new Random();
        int seed = random.nextInt(8);
        int randomInt = random.nextInt(22);
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Patient::getPatientId, seed, seed + randomInt);

        return patientMapper.selectList(queryWrapper);
    }

    /**
     * 接受一条队列消息处理并返回
     *
     * @param queueName
     * @return
     */
    @Override
    public Patient processPatientQueue(String queueName) {
        Patient patient = (Patient) rabbitTemplate.receiveAndConvert(queueName);
        if (patient != null) {
            System.out.println(patient.getPatientName() + "出列");
        }
        return patient;
    }

    @Override
    public int insertPatient(Patient patient) {
        return patientMapper.insert(patient);
    }

    @Override
    public int updatePatient(Patient patient) {
        return patientMapper.updateById(patient);
    }

    @Override
    public int deletePatient(Long id) {
        return patientMapper.deleteById(id);
    }

}
