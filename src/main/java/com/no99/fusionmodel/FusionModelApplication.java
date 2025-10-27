package com.no99.fusionmodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FusionModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(FusionModelApplication.class, args);
    }

}
