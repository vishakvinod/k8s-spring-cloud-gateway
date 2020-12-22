package com.example.chaos.monkey.shopping.bestseller.fashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BestsellerFashionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestsellerFashionApplication.class);
    }
}
