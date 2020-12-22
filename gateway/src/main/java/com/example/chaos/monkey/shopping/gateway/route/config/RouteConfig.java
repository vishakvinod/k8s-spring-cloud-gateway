package com.example.chaos.monkey.shopping.gateway.route.config;

import com.example.chaos.monkey.shopping.domain.Product;
import com.example.chaos.monkey.shopping.gateway.route.dto.RouteDetails;
import com.example.chaos.monkey.shopping.gateway.route.dto.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class RouteConfig {

    @Bean
    @RefreshScope
    public RouteLocator routeLocator(RouteLocatorBuilder builder, Routes routes) {
        log.info("Creating route locator......[START]");
        RouteLocatorBuilder.Builder routeBuilder = builder.routes();
        List<RouteDetails> routeDetailsList = routes.getRoutes();
        log.info("route size : {}", routeDetailsList.size());
        if (routeDetailsList != null && !routeDetailsList.isEmpty()) {
            routeDetailsList.stream().forEach(routeDetails -> {
                log.info("Adding routes for {}", routeDetails.getName());
                routeBuilder.route(ps ->
                        ps.path(routeDetails.getRoute())
                                .filters(f -> f.hystrix(c -> c.setName(routeDetails.getName()).setFallbackUri("forward:/fallback")))
                                .uri("lb://" + routeDetails.getServiceId()));
            });
        }
        return routeBuilder.build();
    }

    @GetMapping("/fallback")
    public ResponseEntity<List<Product>> fallback() {
        log.warn("fallback enabled");
        HttpHeaders headers = new HttpHeaders();
        headers.add("fallback", "true");
        return ResponseEntity.ok().headers(headers).body(Collections.emptyList());
    }
}
