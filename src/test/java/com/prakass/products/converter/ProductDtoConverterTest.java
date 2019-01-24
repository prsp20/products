package com.prakass.products.converter;

import com.fasterxml.jackson.databind.node.TextNode;
import com.prakass.products.client.dto.ColorSwatchDto;
import com.prakass.products.client.dto.PriceDto;
import com.prakass.products.client.dto.ProductDto;
import com.prakass.products.domain.ColorSwatch;
import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ProductDtoConverterTest {

    private ProductDtoConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ProductDtoConverter();
    }

    @Test
    public void convertProductDtoToProduct_basicFields() throws Exception {
        // Given
        ProductDto productDto = new ProductDto("1", "title", createEmptyPriceDto(), new ArrayList<>());

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getProductId(), is("1"));
        assertThat(product.getTitle(), is("title"));
    }

    @Test
    public void convertProductDtoToProduct_colorSwatch() throws Exception {
        // Given
        ColorSwatchDto colorSwatchDto1 = new ColorSwatchDto("Red etc", "Blue", "dummy");
        ColorSwatchDto colorSwatchDto2 = new ColorSwatchDto("Black etc", "Unknown", "dummy1");
        ProductDto productDto = new ProductDto("2", "dummy", createEmptyPriceDto(), Arrays.asList(colorSwatchDto1, colorSwatchDto2));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        ColorSwatch colorSwatch1 = product.getColorSwatches().get(0);
        ColorSwatch colorSwatch2 = product.getColorSwatches().get(1);
        assertThat(colorSwatch1.getColor(), is("Red etc"));
        assertThat(colorSwatch1.getRgbColor(), is("#0000FF"));
        assertThat(colorSwatch1.getSkuid(), is("dummy"));
        assertThat(colorSwatch2.getRgbColor(), is("Unknown"));
    }

    @Test
    public void convertProductDtoToProduct_nowPriceWithIntegerLessThan10() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("5", "", "", "3"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getNowPrice(), is("£3.00"));
    }

    @Test
    public void convertProductDtoToProduct_nowPriceWithDoubleLessThan10() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("5.1111", "", "", "2.2222"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getNowPrice(), is("£2.22"));
    }

    @Test
    public void convertProductDtoToProduct_nowPriceWithIntegerGreaterThan10() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("100", "", "", "80"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getNowPrice(), is("£80"));
    }

    @Test
    public void convertProductDtoToProduct_nowPriceWithDoubleGreaterThan10() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("200.222", "", "", "100.111"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getNowPrice(), is("£100.11"));
    }

    @Test
    public void convertProductDtoToProduct_priceLabelWithShowWasNow() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("2", "", "", "1"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getPriceLabel(), is("Was £2.00, now £1.00"));
    }

    @Test
    public void convertProductDtoToProduct_priceLabelWithShowWasThenWithThen2() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("20.22", "15.22", "12.22", "10.00"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasThenNow);

        // Then
        assertThat(product.getPriceLabel(), is("Was £20.22, then £12.22, now £10"));
    }

    @Test
    public void convertProductDtoToProduct_priceLabelWithShowWasThenWithOnlyThen1() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("20.22", "15.22", "", "10.00"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasThenNow);

        // Then
        assertThat(product.getPriceLabel(), is("Was £20.22, then £15.22, now £10"));
    }

    @Test
    public void convertProductDtoToProduct_priceLabelWithShowWasThenWithMissingThenPrice() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("20.22", "", "", "10.00"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasThenNow);

        // Then
        assertThat(product.getPriceLabel(), is("Was £20.22, now £10"));
    }

    @Test
    public void convertProductDtoToProduct_priceLabelWithShowPercDscount() throws Exception {
        // Given
        ProductDto productDto = createProductDto(createPriceDto("20.00", "", "", "10.00"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowPercDscount);

        // Then
        assertThat(product.getPriceLabel(), is("50% off, now £10"));
    }

    @Test
    public void convertProductDtoToProduct_priceWithUsd() throws Exception {
        // Given
        ProductDto productDto = createProductDto(new PriceDto("20", "", "", new TextNode("10"), "", "USD"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getPriceLabel(), is("Was $20, now $10"));
    }

    @Test
    public void convertProductDtoToProduct_priceWithUnknownCurrency() throws Exception {
        // Given
        ProductDto productDto = createProductDto(new PriceDto("20", "", "", new TextNode("10"), "", "CHF"));

        // When
        Product product = converter.convertProductDtoToProduct(productDto, PriceLabelType.ShowWasNow);

        // Then
        assertThat(product.getPriceLabel(), is("Was CHF20, now CHF10"));
    }

    private ProductDto createProductDto(PriceDto priceDto) {
        return new ProductDto("dummy", "dummy", priceDto, new ArrayList<>());
    }

    private PriceDto createEmptyPriceDto() {
        return new PriceDto("", "", "", new TextNode(""), "", "GBP");
    }

    private PriceDto createPriceDto(String was, String then1, String then2, String now) {
        return new PriceDto(was, then1, then2, new TextNode(now), "", "GBP");
    }
}
