package edu.hbmu.outpatient;

import edu.hbmu.outpatient.dao.DiseaseMapper;
import edu.hbmu.outpatient.dao.MedicineMapper;
import edu.hbmu.outpatient.dao.PatientMapper;
import edu.hbmu.outpatient.domain.entity.Disease;
import edu.hbmu.outpatient.domain.entity.Medicine;
import edu.hbmu.outpatient.domain.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
public class DateLoaderTests {
    private static final String PATH = "D:\\Projects\\JavaProject\\MediMind\\outpatient-service\\src\\main\\resources\\dict\\";

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Test
    void loadingDiseaseAndDescription() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(PATH + "disease_description.txt"));
        String line = null;
        while ((line = br.readLine()) != null) {
            String name = null;
            String description = null;
            try {
                name = line.split(",")[0];
                description = line.split(",")[1];
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            Disease disease = new Disease();
            disease.setDiseaseName(name);
            disease.setDiseaseDescription(description);

//            diseaseMapper.insert(disease);
            System.out.println(name + "写入成功！");
        }
        br.close();
    }

    @Test
    void loadingMedicineAndDescription() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(PATH + "medicine_description.txt"));
        String line = null;
        while ((line = br.readLine()) != null) {
            String name = null;
            String description = null;
            try {
                name = line.split(",")[0];
                description = line.split(",")[1];
            } catch (Exception e) {
                System.err.println("写入错误！" + e.getMessage());
            }
            Medicine medicine = new Medicine();
            medicine.setMedicineName(name);
            medicine.setMedicineDescription(description);

//            medicineMapper.insert(medicine);
            System.err.println(name + "写入成功！");
        }
        br.close();
    }

    @Test
    void loadingPatient() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(PATH + "patient.txt"));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            Patient patient = new Patient();
            patient.setPatientName(split[0]);
            patient.setPatientGender(split[1]);
            patient.setPatientAge(split[2]);
            patient.setPatientPhonenumber(split[3]);

//            patientMapper.insert(patient);
            System.err.println(split[0] + "写入成功！");
        }
        br.close();
    }
}
