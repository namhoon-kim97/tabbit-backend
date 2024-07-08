package com.jungle.Tabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(TabbitApplication.class, args);
    }
}
