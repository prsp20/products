package com.prakass.products.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Product {

    private String productId, title, nowPrice, priceLabel;
    private List<ColorSwatch> colorSwatches;

    @JsonCreator
    public Product(@JsonProperty("productId") String productId, @JsonProperty("title") String title,
                   @JsonProperty("nowPrice") String nowPrice, @JsonProperty("priceLabel") String priceLabel,
                   @JsonProperty("colorSwatches") List<ColorSwatch> colorSwatches) {
        this.productId = productId;
        this.title = title;
        this.nowPrice = nowPrice;
        this.priceLabel = priceLabel;
        this.colorSwatches = colorSwatches;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public List<ColorSwatch> getColorSwatches() {
        return colorSwatches;
    }
}
