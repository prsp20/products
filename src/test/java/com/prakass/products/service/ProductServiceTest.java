package com.prakass.products.service;

import com.fasterxml.jackson.databind.node.TextNode;
import com.prakass.products.client.ProductServiceClient;
import com.prakass.products.client.dto.PriceDto;
import com.prakass.products.client.dto.ProductDto;
import com.prakass.products.converter.ProductDtoConverter;
import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;
    private ProductDtoConverter productDtoConverter;
    private ProductServiceClient productServiceClient;

    @Before
    public void setUp() throws Exception {
        productDtoConverter = mock(ProductDtoConverter.class);
        productServiceClient = mock(ProductServiceClient.class);
        productService = new ProductServiceImpl(productServiceClient, productDtoConverter);
    }

    @Test
    public void shouldReturnOnlyPriceReducedProductsSortedByReductionAmount() throws Exception {
        // Given
        List<ProductDto> productDtoList = Arrays.asList(
                createProductDto("1", "", "10"), // should be excluded
                createProductDto("2", "10", "20"), // should be excluded
                createProductDto("3", "30", "25"),  // should be included, third place
                createProductDto("4", "30", "30"),  // should be excluded
                createProductDto("5", "100", "50"),  // should be included, first place
                createProductDto("6", "", "40"),  // should be excluded
                createProductDto("7", "50", "40")  // should be included, second place
        );
        when(productServiceClient.getProducts()).thenReturn(productDtoList);

        // When
        List<Product> list = productService.getPriceReducedProducts(PriceLabelType.ShowPercDscount);

        // Then
        verify(productDtoConverter, times(3)).convertProductDtoToProduct(any(), any());
        InOrder inOrder = inOrder(productDtoConverter);
        inOrder.verify(productDtoConverter).convertProductDtoToProduct(
                argThat((arg) -> arg.getProductId().equals("5")),
                argThat((arg) -> arg == PriceLabelType.ShowPercDscount));
        inOrder.verify(productDtoConverter).convertProductDtoToProduct(
                argThat((arg) -> arg.getProductId().equals("7")),
                argThat((arg) -> arg == PriceLabelType.ShowPercDscount));
        inOrder.verify(productDtoConverter).convertProductDtoToProduct(
                argThat((arg) -> arg.getProductId().equals("3")),
                argThat((arg) -> arg == PriceLabelType.ShowPercDscount));
        assertThat(list.size(), is(3));
    }

    private ProductDto createProductDto(String id, String wasPrice, String nowPrice) {
        PriceDto priceDto = new PriceDto(wasPrice, "", "", new TextNode(nowPrice), "", "GBP");
        return new ProductDto(id, "", priceDto, new ArrayList<>());
    }
}
