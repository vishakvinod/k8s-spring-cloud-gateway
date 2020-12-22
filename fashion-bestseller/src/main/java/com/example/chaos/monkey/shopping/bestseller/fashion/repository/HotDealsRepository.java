package com.example.chaos.monkey.shopping.bestseller.fashion.repository;

import com.example.chaos.monkey.shopping.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "hot-deals")
public interface HotDealsRepository {

    @GetMapping("/hotdeals/")
    List<Product> getData();
}
