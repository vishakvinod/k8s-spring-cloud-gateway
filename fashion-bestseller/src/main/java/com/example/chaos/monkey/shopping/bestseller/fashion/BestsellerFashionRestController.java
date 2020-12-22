package com.example.chaos.monkey.shopping.bestseller.fashion;

import com.example.chaos.monkey.shopping.bestseller.fashion.repository.HotDealsRepository;
import com.example.chaos.monkey.shopping.domain.Product;
import com.example.chaos.monkey.shopping.domain.ProductBuilder;
import com.example.chaos.monkey.shopping.domain.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/fashion")
public class BestsellerFashionRestController {

    @Autowired
    private HotDealsRepository repository;

    @GetMapping("/bestseller")
    public List<Product> getBestsellerProducts() {
        AtomicLong aLong = new AtomicLong(4);

        ProductBuilder productBuilder = new ProductBuilder();

        Product product1 = productBuilder.setCategory(ProductCategory.FASHION).setId(aLong.getAndIncrement()).setName("Bob Mailor Slim Jeans")
                .createProduct();

        Product product2 = productBuilder.setCategory(ProductCategory.FASHION).setId(aLong.getAndIncrement()).setName("Lewi's Jeanshose 511 " +
                "Slim Fit")
                .createProduct();

        Product product3 = productBuilder.setCategory(ProductCategory.FASHION).setId(aLong.getAndIncrement()).setName("Urban Classics T-Shirt " +
                "Shaped Long Tee")
                .createProduct();
        return Arrays.asList(product1, product2, product3);
    }

    @GetMapping("/bestseller/hotdeals")
    public List<Product> getHotdealProducts() {
        return repository.getData();
    }

}
