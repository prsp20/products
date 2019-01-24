package com.prakass.products.client;

import com.prakass.products.client.dto.ProductDto;
import com.prakass.products.client.dto.ProductWrapperDto;
import com.prakass.products.config.ConfigValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ProductServiceClientImpl implements ProductServiceClient {

    private RestTemplate restTemplate;
    private ConfigValues configValues;

    @Autowired
    public ProductServiceClientImpl(RestTemplate restTemplate, ConfigValues configValues) {
        this.restTemplate = restTemplate;
        this.configValues = configValues;
    }

    @Override
    public List<ProductDto> getProducts() {
        String requestUrl = String.format("%s/categories/%s/products?key=%s", configValues.getProductServiceBaseUrl(),
                configValues.getProductCategoryIncluded(), configValues.getProductServiceKey());
        return restTemplate.getForObject(requestUrl, ProductWrapperDto.class).getProductDtoList();
    }
}
