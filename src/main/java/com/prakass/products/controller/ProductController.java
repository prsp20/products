package com.prakass.products.controller;

import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;
import com.prakass.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getPriceReducedProducts(
            @RequestParam(value = "priceLabel", required = false, defaultValue = "ShowWasNow") PriceLabelType priceLabelType) {
        priceLabelType = priceLabelType == null ? PriceLabelType.ShowWasNow : priceLabelType;
        return new ResponseEntity<>(productService.getPriceReducedProducts(priceLabelType), HttpStatus.OK);
    }

}
