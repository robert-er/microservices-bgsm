package com.bgsm.guiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GuiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuiServiceApplication.class, args);
    }

}
