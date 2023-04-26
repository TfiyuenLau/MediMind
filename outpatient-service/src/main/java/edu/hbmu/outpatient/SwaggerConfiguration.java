package edu.hbmu.outpatient;

import com.fasterxml.classmate.TypeResolver;
import edu.hbmu.outpatient.domain.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Autowired
    private TypeResolver typeResolver;

    @Bean(name = "swagger")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")//请求路径:http://localhost:8081/outpatient/swagger-ui.html#/
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.hbmu.outpatient"))
                .paths(PathSelectors.any())//所有包下所有请求
                .build()
                .additionalModels(typeResolver.resolve(Department.class),//扫描实体类显示在Models中
                        typeResolver.resolve(Diagnosis.class),
                        typeResolver.resolve(Disease.class),
                        typeResolver.resolve(Doctor.class),
                        typeResolver.resolve(Medicine.class),
                        typeResolver.resolve(Patient.class));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MediMind门诊模块接口文档")
                .description("本文档用于outpatient门诊模块API的接口交接...如有疑问请向 tfiyuenlau@foxmail.com 核查")
                .version("v23-04-10")
                .build();
    }

}
