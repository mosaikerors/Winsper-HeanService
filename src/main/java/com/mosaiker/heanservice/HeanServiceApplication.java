package com.mosaiker.heanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class HeanServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeanServiceApplication.class, args);
    }

}
