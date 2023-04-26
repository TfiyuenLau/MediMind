package edu.hbmu.cooperation;

import edu.hbmu.fegin.client.OutpatientClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("edu.hbmu.cooperation.dao")
@EnableFeignClients(basePackageClasses = {OutpatientClient.class})
public class CooperationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CooperationServiceApplication.class, args);
    }

}
