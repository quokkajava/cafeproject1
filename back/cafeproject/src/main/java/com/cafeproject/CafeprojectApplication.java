package com.cafeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CafeprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeprojectApplication.class, args);
    }

}
