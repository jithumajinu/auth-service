package com.openapi.cloud.core.repository.specification;

import com.openapi.cloud.core.model.dto.request.ProductPageCondition;
import com.openapi.cloud.core.model.dto.request.ProductSortBy;
import com.openapi.cloud.core.model.entities.Product;
import com.openapi.cloud.core.model.entities.Product_;
import com.acid.core.web.model.audit.DateAudit_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecification extends AbstractSpecifications {

    public static Specification<Product> getPageByCondition(ProductPageCondition condition) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addKeywordPredicate(condition, root, cb, predicates);
            addDeleteFlagPredicate(root, cb, predicates);

            query.distinct(true);
            addSortCriteria(root, query, cb, condition.getSortItem());

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addKeywordPredicate(ProductPageCondition condition,
                                            Root<Product> root,
                                            CriteriaBuilder cb,
                                            List<Predicate> predicates) {
        if (StringUtils.isNotBlank(condition.getKeyword())) {
            String keyword = createLikePattern(condition.getKeyword());
            Predicate namePredicate = cb.like(root.get(Product_.name), keyword);
            predicates.add(namePredicate);
        }
    }

    private static String createLikePattern(String keyword) {
        return String.format("%%%s%%", keyword);
    }

    private static void addDeleteFlagPredicate(Root<Product> root,
                                               CriteriaBuilder cb,
                                               List<Predicate> predicates) {
        predicates.add(cb.equal(root.get(DateAudit_.deleteFlag), DELETE_FLAG));
    }

    private static void addSortCriteria(Root<Product> root,
                                        CriteriaQuery<?> query,
                                        CriteriaBuilder cb,
                                        ProductSortBy sortItem) {
        Order order = createSortOrder(root, cb, sortItem);
        query.orderBy(order);
    }

    private static Order createSortOrder(Root<Product> root,
                                         CriteriaBuilder cb,
                                         ProductSortBy sortItem) {
        if (Objects.isNull(sortItem)) {
            return cb.asc(root.get(Product_.id));
        }

        return switch (sortItem) {
            case PRODUCT_ID_DESC -> cb.desc(root.get(Product_.id));
            case PRODUCT_NAME_ASC -> cb.asc(root.get(Product_.name));
            case PRODUCT_NAME_DESC -> cb.desc(root.get(Product_.name));
            default -> cb.asc(root.get(Product_.id));
        };
    }
}
