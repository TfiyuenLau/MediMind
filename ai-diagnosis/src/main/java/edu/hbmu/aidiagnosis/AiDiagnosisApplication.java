package edu.hbmu.aidiagnosis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class AiDiagnosisApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiDiagnosisApplication.class, args);
    }

}
