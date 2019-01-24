package com.prakass.products.service;

import com.prakass.products.client.ProductServiceClient;
import com.prakass.products.converter.ProductDtoConverter;
import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductServiceClient productServiceClient;
    private ProductDtoConverter productDtoConverter;

    @Autowired
    public ProductServiceImpl(ProductServiceClient productServiceClient, ProductDtoConverter productDtoConverter) {
        this.productServiceClient = productServiceClient;
        this.productDtoConverter = productDtoConverter;
    }

    @Override
    public List<Product> getPriceReducedProducts(PriceLabelType priceLabelType) {
        return productServiceClient.getProducts()
                .stream()
                .filter(productDto -> productDto.getPriceDto().getPriceReduction() > 0)
                .sorted((one, other) -> Double.compare(other.getPriceDto().getPriceReduction(),
                        one.getPriceDto().getPriceReduction()))
                .map(productDto -> productDtoConverter.convertProductDtoToProduct(productDto, priceLabelType))
                .collect(Collectors.toList());
    }
}
