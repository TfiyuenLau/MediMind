package edu.hbmu.outpatient;

import edu.hbmu.outpatient.constants.MqConstants;
import edu.hbmu.outpatient.domain.entity.Patient;
import edu.hbmu.outpatient.service.IPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Properties;

@SpringBootTest
public class RabbitMqTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private IPatientService patientService;

    @Test
    void publishPatientInfo() {
        List<Patient> registerPatientList = patientService.publishRegisterPatientList();
        for (Patient patient : registerPatientList) {
            System.out.println(patient.getPatientId() + "." + patient.getPatientName() + "完成了挂号...");
            rabbitTemplate.convertAndSend(MqConstants.PATIENT_QUEUE, patient);
        }
    }

    @Test
    void clearPatientQueue() {
        Properties queueProperties = amqpAdmin.getQueueProperties(MqConstants.PATIENT_QUEUE);
        assert queueProperties != null;
        int count = (Integer) queueProperties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
        System.out.println(count);
        for (int i = 0; i < count; i++) {
            Patient patient = (Patient) rabbitTemplate.receiveAndConvert(MqConstants.PATIENT_QUEUE);
            assert patient != null;
            System.out.println(patient.getPatientId() + "." + patient.getPatientName() + "——消息已被消费");
        }
    }

    @Test
    void testProcessPatientInfo() {
        Patient patient = patientService.processPatientQueue(MqConstants.PATIENT_QUEUE);
        System.out.println(patient.getPatientId() + "." + patient.getPatientName() + "——消息已被消费");
    }

}
