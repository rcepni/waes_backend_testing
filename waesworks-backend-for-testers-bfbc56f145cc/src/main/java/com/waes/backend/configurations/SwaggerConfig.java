package com.waes.backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.singletonList;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("waes-users-api-1")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.waes.backend.controllers"))
        .build()
        .apiInfo(new ApiInfoBuilder().title("WAES Heroes API")
                                     .description("An API for WAES users")
                                     .version("1")
                                     .build())
        .consumes(new HashSet<>(singletonList(APPLICATION_JSON_VALUE)))
        .produces(new HashSet<>(singletonList(APPLICATION_JSON_VALUE)));
  }
}