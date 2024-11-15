package com.propvue.fulfillmentmanager.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    @NotNull
    private String productId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductStatus status;

    @Column(name = "fulfillment_center", nullable = false)
    @NotNull
    private String fulfillmentCenter;

    @Column(name = "quantity")
    @Min(0)
    private int quantity;

    @Column(name = "value")
    @Min(0)
    private double value;

    public Product(String productId, String string, String fulfillmentCenter, int quantity, double value) {
        this.productId = productId;
        this.fulfillmentCenter = fulfillmentCenter;
        this.quantity = quantity;
        this.value = value;
    }

    public Product(String productId, ProductStatus status, String fulfillmentCenter, int quantity, double value) {
        this.productId = productId;
        this.status = status;
        this.fulfillmentCenter = fulfillmentCenter;
        this.quantity = quantity;
        this.value = value;
    }
}
