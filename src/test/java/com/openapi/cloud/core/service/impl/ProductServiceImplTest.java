package com.openapi.cloud.core.service.impl;

import com.acid.core.web.constants.DeleteFlag;
import com.acid.core.web.constants.PagingSize;
import com.openapi.cloud.core.exception.ResourceNotFoundException;
import com.acid.core.web.model.ModelPage;
import com.openapi.cloud.core.model.dto.ProductDto;
import com.openapi.cloud.core.model.dto.request.GetAllProductRequest;
import com.openapi.cloud.core.model.dto.request.ProductSortBy;
import com.openapi.cloud.core.model.entities.Product;
import com.openapi.cloud.core.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testGetProductById_NotFound() {
        // Mock repository to return empty
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Verify exception is thrown
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetProductList() {
        // Create test data
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        
        // Mock repository
        when(productRepository.findAllByDeleteFlag(DeleteFlag.ACTIVE)).thenReturn(productList);
        
        // Call the service method
        List<ProductDto> result = productService.getProductList();
        
        // Verify
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(productRepository, times(1)).findAllByDeleteFlag(DeleteFlag.ACTIVE);
    }

    @Test
    void testGetProductPage() {
        // Create test data
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Page<Product> page = new PageImpl<>(productList);
        
        // Create request
        GetAllProductRequest request = mock(GetAllProductRequest.class);
        when(request.getKeyword()).thenReturn("test");
        when(request.getSortBy()).thenReturn(ProductSortBy.PRODUCT_NAME_ASC);
        when(request.getPage()).thenReturn(1);
        when(request.getPagingSize()).thenReturn(PagingSize.SIZE_10);
        
        // Mock repository
        when(productRepository.findPageByCondition(any(), any())).thenReturn(page);
        
        // Call the service method
        ModelPage<ProductDto> result = productService.getProductPage(request);
        
        // Verify
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        verify(productRepository, times(1)).findPageByCondition(any(), any());
    }
}