package com.example.chaos.monkey.shopping.hotdeals.domain;

import com.example.chaos.monkey.shopping.domain.response.ProductResponse;

public class StartPage {
    private long duration;
    private String statusFashion;
    private String statusToys;
    private ProductResponse fashionResponse;
    private ProductResponse toysResponse;

    public String getStatusFashion() {
        return statusFashion;
    }

    public void setStatusFashion(String status) {
        this.statusFashion = status;
    }

    public String getStatusToys() {
        return statusToys;
    }

    public void setStatusToys(String statusToys) {
        this.statusToys = statusToys;
    }

    public ProductResponse getFashionResponse() {
        return fashionResponse;
    }

    public void setFashionResponse(ProductResponse fashionResponse) {
        this.setStatusFashion(fashionResponse.getResponseType().name());
        this.fashionResponse = fashionResponse;
    }

    public ProductResponse getToysResponse() {
        return toysResponse;
    }

    public void setToysResponse(ProductResponse toysResponse) {
        this.setStatusToys(toysResponse.getResponseType().name());
        this.toysResponse = toysResponse;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
