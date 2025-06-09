package com.openapi.cloud.core.service;

import com.acid.core.web.model.ModelPage;
import com.openapi.cloud.core.model.dto.ProductDto;
import com.openapi.cloud.core.model.dto.request.GetAllProductRequest;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto product);

    ProductDto getProductById(Long productId);

    List<ProductDto> getProductList();

    ModelPage<ProductDto> getProductPage(GetAllProductRequest request);
}
