package com.propvue.fulfillmentmanager.repository;

import com.propvue.fulfillmentmanager.model.Product;
import com.propvue.fulfillmentmanager.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByFulfillmentCenter(String fulfillmentCenter, Pageable pageable);


    double sumValueByStatus(ProductStatus status);

    Product findByProductId(String productId);
}
