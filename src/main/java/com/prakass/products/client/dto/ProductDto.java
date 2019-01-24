package com.prakass.products.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductDto {
    private String productId, title;
    private PriceDto priceDto;
    private List<ColorSwatchDto> colorSwatchDtoList;

    @JsonCreator
    public ProductDto(@JsonProperty("productId") String productId, @JsonProperty("title")String title,
                      @JsonProperty("price")PriceDto priceDto,
                      @JsonProperty("colorSwatches")List<ColorSwatchDto> colorSwatchDtoList) {
        this.productId = productId;
        this.title = title;
        this.priceDto = priceDto;
        this.colorSwatchDtoList = colorSwatchDtoList;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public PriceDto getPriceDto() {
        return priceDto;
    }

    public List<ColorSwatchDto> getColorSwatchDtoList() {
        return colorSwatchDtoList;
    }
}
