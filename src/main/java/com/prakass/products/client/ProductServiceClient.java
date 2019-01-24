package com.prakass.products.client;


import com.prakass.products.client.dto.ProductDto;

import java.util.List;

public interface ProductServiceClient {

    List<ProductDto> getProducts();

}
