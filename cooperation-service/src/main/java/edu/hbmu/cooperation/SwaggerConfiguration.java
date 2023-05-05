package edu.hbmu.cooperation;

import com.fasterxml.classmate.TypeResolver;
import edu.hbmu.cooperation.domain.request.MsgInfoParams;
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
                .pathMapping("/")//请求路径:http://localhost:8083/cooperation/swagger-ui.html#/
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.hbmu.cooperation"))
                .paths(PathSelectors.any())//所有包下所有请求
                .build().additionalModels(typeResolver.resolve(MsgInfoParams.class));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Cooperation Service协作模块接口文档")
                .description("本文档用于MediMind的Cooperation团队协作模块API的接口交接...如有疑问请向 tfiyuenlau@foxmail.com 核查")
                .version("v23-04-19")
                .build();
    }

}
