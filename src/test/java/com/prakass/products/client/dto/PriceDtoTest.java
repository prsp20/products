package com.prakass.products.client.dto;

import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceDtoTest {

    @Test
    public void getWasPrice() throws Exception {
        PriceDto priceDto = new PriceDto("11.11", "2", "3", new TextNode("2"), "", "GBP");
        assertThat(priceDto.getWasPrice(), is(11.11));
    }

    @Test
    public void getThen1Price() throws Exception {
        PriceDto priceDto = new PriceDto(null, "2", "3", new TextNode("2"), "", "GBP");
        assertThat(priceDto.getThen1Price(), is(2.0));
    }

    @Test
    public void getThen2Price() throws Exception {
        PriceDto priceDto = new PriceDto(null, "2", "3", new TextNode("2"), "", "GBP");
        assertThat(priceDto.getThen2Price(), is(3.0));
    }

    @Test
    public void getNowPrice() throws Exception {
        PriceDto priceDto = new PriceDto(null, "2", "3", new TextNode("11.11"), "", "GBP");
        assertThat(priceDto.getNowPrice(), is(11.11));
    }

    @Test
    public void getPriceReduction_noReduction() {
        PriceDto priceDto = new PriceDto(null, "2", "3", new TextNode("12.3"), "", "GBP");
        assertThat(priceDto.getPriceReduction(), is(0.0));
    }

    @Test
    public void getPriceReduction_positiveReduction() {
        PriceDto priceDto = new PriceDto("13.2", "2", "3", new TextNode("12.1"), "", "GBP");
        assertThat(priceDto.getPriceReduction(), is(1.1));
    }

    @Test
    public void getPriceReduction_negativeReduction() {
        PriceDto priceDto = new PriceDto("10.0", "2", "3", new TextNode("12.12"), "", "GBP");
        assertThat(priceDto.getPriceReduction(), is(-2.12));
    }
}
