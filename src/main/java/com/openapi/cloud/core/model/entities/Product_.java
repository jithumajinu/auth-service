package com.openapi.cloud.core.model.entities;

import com.acid.core.web.model.audit.DateAudit_;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@SuppressWarnings("squid:S00101") // Class name with special characters
@StaticMetamodel(Product.class)
public class Product_ extends DateAudit_ {

    public static SingularAttribute<Product, Long> id;

    public static SingularAttribute<Product, String> name;

}
