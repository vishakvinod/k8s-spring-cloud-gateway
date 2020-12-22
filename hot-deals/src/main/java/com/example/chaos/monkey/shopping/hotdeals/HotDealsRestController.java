package com.example.chaos.monkey.shopping.hotdeals;


import com.example.chaos.monkey.shopping.domain.Product;
import com.example.chaos.monkey.shopping.domain.ProductBuilder;
import com.example.chaos.monkey.shopping.domain.ProductCategory;
import com.example.chaos.monkey.shopping.domain.response.ProductResponse;
import com.example.chaos.monkey.shopping.domain.response.ResponseType;
import com.example.chaos.monkey.shopping.hotdeals.domain.StartPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author Benjamin Wilms
 */
@RestController
public class HotDealsRestController {

    @Autowired
    private WebClient client;

    private ProductResponse errorResponse;

    private ParameterizedTypeReference<Product> productParameterizedTypeReference =
            new ParameterizedTypeReference<Product>() {};

    private Function<ClientResponse, Mono<ProductResponse>> responseProcessor = clientResponse -> {
        HttpHeaders headers = clientResponse.headers().asHttpHeaders();
        if(headers.containsKey("fallback") && headers.get("fallback").contains("true")) {
            return Mono.just(new ProductResponse(ResponseType.FALLBACK, Collections.emptyList()));
        }
        return clientResponse.bodyToFlux(productParameterizedTypeReference).collectList()
                .flatMap(products -> Mono.just(new ProductResponse(ResponseType.REMOTE_SERVICE, products)));
    };

    public HotDealsRestController() {
        this.errorResponse = new ProductResponse();
        errorResponse.setResponseType(ResponseType.ERROR);
        errorResponse.setProducts(Collections.emptyList());
    }

    @GetMapping("/hotdeals/")
    public List<Product> getHotDeals() {
        AtomicLong aLong = new AtomicLong(7);

        ProductBuilder productBuilder = new ProductBuilder();

        Product product1 = productBuilder.setCategory(ProductCategory.FASHION).setId(aLong.getAndIncrement()).setName("Thermal Winter Warm Hot Heat" +
                " Socks")
                .createProduct();

        Product product2 = productBuilder.setCategory(ProductCategory.TOYS).setId(aLong.getAndIncrement()).setName("RC Quadcopter Drone with 2.0MP Camera Live")
                .createProduct();

        Product product3 = productBuilder.setCategory(ProductCategory.BOOKS).setId(aLong.getAndIncrement()).setName("Spring Boot 2: Moderne Softwareentwicklung mit Spring 5")
                .createProduct();
        return Arrays.asList(product1, product2, product3);
    }

    @GetMapping("/hotdeals/all")
    public Mono<StartPage>  getAllDeals() {
        long start = System.currentTimeMillis();
        Mono<ProductResponse> fashionBestSellers = client.get().uri("/fashion/bestseller").exchange().flatMap(responseProcessor)
                .onErrorResume(t -> {
                    t.printStackTrace();
                    return Mono.just(errorResponse);
                });
        Mono<ProductResponse> toysBestSellers = client.get().uri("/toys/bestseller").exchange().flatMap(responseProcessor)
                .onErrorResume(t -> {
                    t.printStackTrace();
                    return Mono.just(errorResponse);
                });

        Mono<StartPage> page = Mono.zip(fashionBestSellers, toysBestSellers).flatMap(t -> {
            StartPage p = new StartPage();
            ProductResponse fashion = t.getT1();
            ProductResponse toys = t.getT2();
            p.setFashionResponse(fashion);
            p.setToysResponse(toys);
            p.setStatusFashion(fashion.getResponseType().name());
            p.setStatusToys(toys.getResponseType().name());
            // Request duration
            p.setDuration(System.currentTimeMillis() - start);
            return Mono.just(p);
        });

        return page;
    }


}
