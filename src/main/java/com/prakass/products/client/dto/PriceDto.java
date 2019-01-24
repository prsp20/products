package com.prakass.products.client.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.prakass.products.util.NumberUtil;

import java.math.BigDecimal;
import java.math.MathContext;

public class PriceDto {

    private String was, then1, then2, uom, currency;
    private JsonNode now;

    private double wasPrice, then1Price, then2Price, nowPrice, priceReduction;

    @JsonCreator
    public PriceDto(@JsonProperty("was") String was, @JsonProperty("then1") String then1,
                    @JsonProperty("then2") String then2, @JsonProperty("now") JsonNode now,
                    @JsonProperty("uom") String uom, @JsonProperty("currency") String currency) {
        this.was = was;
        this.then1 = then1;
        this.then2 = then2;
        this.now = now;
        this.uom = uom;
        this.currency = currency;
        this.wasPrice = NumberUtil.parseNumberSafely(was);
        this.then1Price = NumberUtil.parseNumberSafely(then1);
        this.then2Price = NumberUtil.parseNumberSafely(then2);
        this.nowPrice = NumberUtil.parseNumberSafely(getNowPriceStr());
        this.priceReduction = this.wasPrice != 0 ? new BigDecimal(this.wasPrice).subtract(new BigDecimal(this.nowPrice), MathContext.DECIMAL32).doubleValue() : 0;

    }

    public String getWas() {
        return was;
    }

    public String getThen1() {
        return then1;
    }

    public String getThen2() {
        return then2;
    }

    public JsonNode getNow() {
        return now;
    }

    public String getNowPriceStr() {
        if (now != null) {
            return now.isObject() ? now.get("to").textValue() : now.textValue();
        }

        return "";
    }

    public String getFromPrice() {
        if (now != null) {
            return now.isObject() ? now.get("from").textValue() : "";
        }

        return "";
    }

    public String getUom() {
        return uom;
    }

    public String getCurrency() {
        return currency;
    }

    public double getWasPrice() {
        return wasPrice;
    }

    public double getThen1Price() {
        return then1Price;
    }

    public double getThen2Price() {
        return then2Price;
    }

    public double getNowPrice() {
        return nowPrice;
    }

    public double getPriceReduction() {
        return priceReduction;
    }
}
