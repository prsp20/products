package com.prakass.products.client.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductWrapperDto {

    private List<ProductDto> productDtoList;

    @JsonCreator
    public ProductWrapperDto(@JsonProperty("products") List<ProductDto> productDtoList) {
        this.productDtoList = productDtoList;
    }

    public List<ProductDto> getProductDtoList() {
        return productDtoList;
    }
}
