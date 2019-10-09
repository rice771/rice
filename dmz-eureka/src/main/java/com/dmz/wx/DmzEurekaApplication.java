package com.dmz.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEurekaServer
@SpringBootApplication
public class DmzEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmzEurekaApplication.class, args);
    }

}
