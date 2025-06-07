package com.energycommunities.communityproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommunityProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityProducerApplication.class, args);
    }
}