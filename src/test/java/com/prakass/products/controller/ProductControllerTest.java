package com.prakass.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;
import com.prakass.products.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturn400ForInvalidPriceLabelType() throws Exception {
        HttpClientErrorException exception = null;
        try {
            restTemplate.exchange(buildUrl("invalid"), HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Product>>() {
                    });
        } catch (HttpClientErrorException ex) {
            exception = ex;
        }
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(exception.getResponseHeaders().getContentType().getType(), is(MediaType.APPLICATION_JSON.getType()));
    }

    @Test
    public void shouldSetDefaultPriceLabelTypeIfNotSpecified() throws Exception {
        restTemplate.exchange(buildUrl(null), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {
                });
        verify(productService).getPriceReducedProducts(PriceLabelType.ShowWasNow);
    }

    @Test
    public void shouldReturnListOfProducts() throws Exception {
        Product product1 = new Product("1", "title", "11.11", "exampleLabel", new ArrayList<>());
        Product product2 = new Product("2", "title2", "22.22", "exampleLabel2", new ArrayList<>());
        when(productService.getPriceReducedProducts(any())).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<List<Product>> response = restTemplate.exchange(buildUrl("ShowWasThenNow"), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {
                });

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getHeaders().getContentType().getType(), is(MediaType.APPLICATION_JSON.getType()));
        assertThat(response.getBody().size(), is(2));
        assertThat(toJsonString(response.getBody()), is(toJsonString(Arrays.asList(product1, product2))));
        verify(productService).getPriceReducedProducts(PriceLabelType.ShowWasThenNow);
    }

    @Test
    public void shouldReturn500IfAnExceptionIsThrown() throws Exception {
        when(productService.getPriceReducedProducts(PriceLabelType.ShowPercDscount))
                .thenThrow(new RuntimeException("Dummy error"));
        HttpServerErrorException exception = null;

        try {
            restTemplate.exchange(buildUrl("ShowPercDscount"), HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Product>>() {
                    });
        } catch (HttpServerErrorException ex) {
            exception = ex;
        }

        assertThat(exception, is(notNullValue()));
        assertThat(exception.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(exception.getResponseHeaders().getContentType().getType(), is(MediaType.APPLICATION_JSON.getType()));
        assertThat(exception.getResponseBodyAsString(), containsString("Dummy error"));
    }

    private String buildUrl(String priceLabelType) {
        String url = String.format("http://localhost:%s/v1/products", port);
        if (priceLabelType != null) {
            url = String.format("%s?priceLabel=%s", url, priceLabelType);
        }

        return url;
    }

    private String toJsonString(Object object) throws Exception {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
