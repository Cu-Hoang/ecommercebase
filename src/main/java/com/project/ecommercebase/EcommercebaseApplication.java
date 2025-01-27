package com.project.ecommercebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EcommercebaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommercebaseApplication.class, args);
    }
}
