package com.xhxj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpingbootEhentaiDownloadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpingbootEhentaiDownloadApplication.class, args);
    }

}

