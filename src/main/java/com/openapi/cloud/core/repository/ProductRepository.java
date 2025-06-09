package com.openapi.cloud.core.repository;

import com.acid.core.web.constants.DeleteFlag;
import com.openapi.cloud.core.model.dto.request.ProductPageCondition;
import com.openapi.cloud.core.model.entities.Product;
import com.openapi.cloud.core.repository.specification.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findAllByDeleteFlag(DeleteFlag flag);

    @Query("SELECT e FROM Product e WHERE e.deleteFlag = com.acid.core.web.constants.DeleteFlag.ACTIVE")
    List<Product> findAllProduct();


    default Page<Product> findPageByCondition(ProductPageCondition condition, Pageable pageable) {
        return this.findAll(ProductSpecification.getPageByCondition(condition), pageable);
    }
}
