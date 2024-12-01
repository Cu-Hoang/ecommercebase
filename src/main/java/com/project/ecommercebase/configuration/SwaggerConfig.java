package com.project.ecommercebase.configuration;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                // .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info()
                        .title("API Document")
                        .version("v0.0.1")
                        .description("IS335.P11")
                        .contact(new Contact()
                                .name("UIT")
                                .email("phongdaotaodh@uit.edu.vn")
                                .url("https://daa.uit.edu.vn/"))
                        .license(new License()))
                .servers(List.of(new Server().url("http://localhost:8080/api/v1")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service-1")
                .packagesToScan("com.project.ecommercebase.controller")
                .build();
    }

    //    private SecurityScheme createAPIKeyScheme() {
    //        return new SecurityScheme()
    //                .name("Bearer Authentication")
    //                .type(SecurityScheme.Type.HTTP)
    //                .bearerFormat("JWT")
    //                .scheme("bearer");
    //    }
}
