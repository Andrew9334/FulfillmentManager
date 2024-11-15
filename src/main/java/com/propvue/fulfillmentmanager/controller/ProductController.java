package com.propvue.fulfillmentmanager.controller;

import com.propvue.fulfillmentmanager.model.Product;
import com.propvue.fulfillmentmanager.model.ProductStatus;
import com.propvue.fulfillmentmanager.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/load-from-sheet")
    public String loadProductsFromSheet(
            @RequestParam String spreadsheetId,
            @RequestParam String range,
            @RequestParam String accessToken) {
        try {
            productService.loadProductsFromSheet(spreadsheetId, range, accessToken);
            return "Products loaded from Google Sheets";
        } catch (IOException e) {
            return "Error loading data: " + e.getMessage();
        }
    }

    @GetMapping("/by-status")
    public ResponseEntity<Page<Product>> getProductsByStatus(
            @RequestParam ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductsByStatus(status, pageable);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/by-fulfillment-center")
    public ResponseEntity<Page<Product>> getProductsByFulfillmentCenter(
            @RequestParam String fulfillmentCenter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProductsByFulfillmentCenter(fulfillmentCenter, pageable);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/total-value-by-status")
    public double getTotalValueByStatus(@RequestParam ProductStatus status) {
        return productService.calculateTotalValueByStatus(status);
    }
}
