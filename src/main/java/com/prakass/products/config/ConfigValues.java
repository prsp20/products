package com.prakass.products.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigValues {
    @Value("${productservice.baseurl}")
    private String productServiceBaseUrl;

    @Value("${productservice.key}")
    private String productServiceKey;

    @Value("${productservice.productcategorytoinclude}")
    private String productCategoryIncluded;

    public String getProductServiceBaseUrl() {
        return productServiceBaseUrl;
    }

    public String getProductServiceKey() {
        return productServiceKey;
    }

    public String getProductCategoryIncluded() {
        return productCategoryIncluded;
    }
}
