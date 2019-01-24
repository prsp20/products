package com.prakass.products.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColorSwatchDto {
    private String color, basicColor, skuId;

    public ColorSwatchDto(@JsonProperty("color") String color, @JsonProperty("basicColor") String basicColor,
                          @JsonProperty("skuId") String skuId) {
        this.color = color;
        this.basicColor = basicColor;
        this.skuId = skuId;
    }

    public String getColor() {
        return color;
    }

    public String getBasicColor() {
        return basicColor;
    }

    public String getSkuId() {
        return skuId;
    }
}
