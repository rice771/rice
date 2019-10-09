package com.rice.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: swagger配置类
 * @Author: ln
 * @Date: 2019/9/16 17:33
 * @Param
 * @Return
 **/
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger2Config {

    @Bean
    public Docket EpsInfoApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("系统信息管理").apiInfo(EpsInfoApiInf()).select()
                .apis(RequestHandlerSelectors.basePackage("com.rice.base.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo EpsInfoApiInf() {
        return new ApiInfoBuilder().title("系统信息管理").termsOfServiceUrl("/base").description("系统信息管理").build();
    }
}
