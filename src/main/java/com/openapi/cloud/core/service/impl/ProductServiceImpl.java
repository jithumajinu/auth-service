package com.openapi.cloud.core.service.impl;

import com.acid.core.web.constants.DeleteFlag;
import com.openapi.cloud.core.exception.ResourceNotFoundException;
import com.openapi.cloud.core.mapper.ProductMapper;
import com.acid.core.web.model.ModelPage;
import com.openapi.cloud.core.model.dto.ProductDto;
import com.openapi.cloud.core.model.dto.request.GetAllProductRequest;
import com.openapi.cloud.core.model.dto.request.ProductPageCondition;
import com.openapi.cloud.core.model.entities.Product;
import com.openapi.cloud.core.repository.ProductRepository;
import com.openapi.cloud.core.service.ProductService;
import com.openapi.cloud.core.util.PageUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {


    @Autowired
    ProductRepository productRepository;

    @Override
    @CacheEvict(value = "productCache", allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating new product and evicting cache");
        Product product = ProductMapper.MAPPER.toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.MAPPER.toDto(savedProduct);
    }

    @Override
    @Cacheable(value = "productCache", key = "#productId")
    public ProductDto getProductById(Long productId) {
        log.info("Fetching product from database for id: {}", productId);
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId));

        return ProductMapper.MAPPER.toDto(product);
    }

    @Override
    public List<ProductDto> getProductList() {
        List<Product> productList = productRepository.findAllByDeleteFlag(DeleteFlag.ACTIVE);
        return ProductMapper.MAPPER.toDtoList(productList);
    }

    @Override
    public ModelPage<ProductDto> getProductPage(GetAllProductRequest request) {
        ProductPageCondition condition = buildSearchCondition(request);
        Pageable pageable = createPageable(request);
        Page<Product> productPage = productRepository.findPageByCondition(condition, pageable);
        return createModelPage(productPage);
    }

    private ProductPageCondition buildSearchCondition(GetAllProductRequest request) {
        return ProductPageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortBy())
                .build();
    }

    private Pageable createPageable(GetAllProductRequest request) {
        return PageUtil.toPageable(
                request.getPage(),
                request.getPagingSize().getCode(),
                Collections.emptyMap());
    }

    private ModelPage<ProductDto> createModelPage(Page<Product> page) {
        List<ProductDto> productList = page.hasContent()
                ? ProductMapper.MAPPER.toDtoList(page.getContent())
                : Collections.emptyList();

        return ModelPage.<ProductDto>builder()
                .content(productList)
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
