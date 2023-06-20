package com.flexcub.resourceplanning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.TimeZone;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableSwagger2
@ComponentScan
@EnableScheduling
@EnableCaching

public class ResourcePlanningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourcePlanningApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("UST"));
    }
}

