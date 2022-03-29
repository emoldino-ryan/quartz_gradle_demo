package com.quartz_gradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.quartz_gradle","com.emoldino"})
public class QuartzGradleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzGradleDemoApplication.class, args);
    }

}
