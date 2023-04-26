package edu.hbmu.outpatient;

import edu.hbmu.outpatient.dao.DoctorMapper;
import edu.hbmu.outpatient.domain.entity.Disease;
import edu.hbmu.outpatient.domain.entity.Doctor;
import edu.hbmu.outpatient.service.IDiseaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class OutpatientServiceApplicationTests {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private IDiseaseService diseaseService;

    @Test
    void contextLoads() {
    }

    /**
     * 测试数据库连接与MyBatisPlus状况
     */
    @Test
    void testDoctorMapper() {
        List<Doctor> doctorList = doctorMapper.selectList(null);
        for (Doctor doctor : doctorList) {
            System.err.println(doctor.getDoctorId() + "." + doctor.getDoctorName() + ":" + doctor.getAuthority());
        }
    }

    @Test
    void testSearchDiseaseByName() throws IOException {
        List<Disease> diseaseList = diseaseService.findDiseasesByName("后发性白内障");
        for (Disease disease : diseaseList) {
            System.out.println(disease.getDiseaseId() + "." +disease.getDiseaseName());
        }
    }

}
