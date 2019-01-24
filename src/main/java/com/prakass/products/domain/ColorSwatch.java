package com.prakass.products.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ColorSwatch {

    private String color, rgbColor, skuid;

    @JsonCreator
    public ColorSwatch(@JsonProperty("color") String color, @JsonProperty("rgbColor") String rgbColor,
                       @JsonProperty("skuid") String skuid) {
        this.color = color;
        this.rgbColor = rgbColor;
        this.skuid = skuid;
    }

    public String getColor() {
        return color;
    }

    public String getRgbColor() {
        return rgbColor;
    }

    public String getSkuid() {
        return skuid;
    }
}
