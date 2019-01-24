package com.prakass.products.service;

import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> getPriceReducedProducts(PriceLabelType priceLabelType);

}
