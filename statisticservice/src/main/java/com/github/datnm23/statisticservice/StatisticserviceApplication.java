package com.github.datnm23.statisticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StatisticserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticserviceApplication.class, args);
    }

}
