package edu.hbmu.aidiagnosis;

import com.fasterxml.classmate.TypeResolver;
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
                .pathMapping("/")//请求路径:http://localhost:8082/aidiagnosis/swagger-ui.html#/
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.hbmu.aidiagnosis"))
                .paths(PathSelectors.any())//所有包下所有请求
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AI Diagnosis辅助诊断模块接口文档")
                .description("本文档用于AI Diagnosis辅助诊断模块API的接口交接...如有疑问请向 tfiyuenlau@foxmail.com 核查")
                .version("v23-04-13")
                .build();
    }

}
