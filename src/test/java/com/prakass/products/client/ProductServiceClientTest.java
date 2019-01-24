package com.prakass.products.client;

import com.prakass.products.client.dto.ColorSwatchDto;
import com.prakass.products.client.dto.PriceDto;
import com.prakass.products.client.dto.ProductDto;
import com.prakass.products.util.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceClientTest {

    private static final String EXAMPLE_URL =
            "http://api.example.com/vx/categories/categoryId/products?key=keyValue";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductServiceClient productServiceClient;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);
    }

    @Test
    public void shouldGetAllProductsForGivenCategory() throws Exception {
        // Given
        String expectedString = FileUtil.readFileFromClasspath("products.json");
        mockServer
                .expect(ExpectedCount.once(), requestTo(EXAMPLE_URL))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(expectedString));

        // When
        List<ProductDto> productDtoList = productServiceClient.getProducts();

        // Then
        assertThat(productDtoList, is(notNullValue()));
        assertThat(productDtoList.size(), is(4));

        // Product basic fields
        ProductDto productDto1 = productDtoList.get(0);
        assertThat(productDto1.getProductId(), is("ID1"));
        assertThat(productDto1.getTitle(), is("DummyTitle"));

        // Color swatch fields
        assertThat(productDto1.getColorSwatchDtoList().size(), is(2));
        ColorSwatchDto colorSwatchDto = productDto1.getColorSwatchDtoList().get(0);
        assertThat(colorSwatchDto.getColor(), is("White/Black Stripe"));
        assertThat(colorSwatchDto.getBasicColor(), is("White"));
        assertThat(colorSwatchDto.getSkuId(), is("237348495"));
        ColorSwatchDto colorSwatchDto1 = productDto1.getColorSwatchDtoList().get(1);
        assertThat(colorSwatchDto1.getColor(), is(nullValue()));
        assertThat(colorSwatchDto1.getBasicColor(), is(nullValue()));
        assertThat(colorSwatchDto1.getSkuId(), is(nullValue()));

        // Price fields
        PriceDto priceDto1 = productDto1.getPriceDto();
        assertThat(priceDto1.getWas(), is("140.00"));
        assertThat(priceDto1.getThen1(), is("130.00"));
        assertThat(priceDto1.getThen2(), is("120.00"));
        assertThat(priceDto1.getNowPriceStr(), is("99.00"));
        assertThat(priceDto1.getFromPrice(), is(""));
        assertThat(priceDto1.getCurrency(), is("GBP"));

        PriceDto priceDto2 = productDtoList.get(2).getPriceDto();
        assertThat(priceDto2.getNowPriceStr(), is("100.00"));
        assertThat(priceDto2.getFromPrice(), is("55.00"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionIfErrorReturnedByProductService() throws Exception {
        // Given
        mockServer.expect(requestTo(EXAMPLE_URL))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON));

        // When
        productServiceClient.getProducts();
    }
}
