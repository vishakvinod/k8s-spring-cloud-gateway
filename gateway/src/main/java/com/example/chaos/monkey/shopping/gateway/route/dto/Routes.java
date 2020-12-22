package com.example.chaos.monkey.shopping.gateway.route.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConfigurationProperties("gateway")
public class Routes {

    List<RouteDetails> routes = new ArrayList<>();
}
