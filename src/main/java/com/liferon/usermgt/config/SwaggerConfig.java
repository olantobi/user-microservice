package com.liferon.usermgt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean
    public Docket offerPlatformAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("tfare-usermgt")
                .apiInfo(apiInfo())                
                .select()                
                .paths(regex("/api.*"))
                .build();
    }
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("TFare User Management Module Microservice API")
                .description("TFare User Management Microservice API")
                .termsOfServiceUrl("http://www.tfare.ng/terms-conditions")                                
                .version("1.0")
                .build();
    }
}
