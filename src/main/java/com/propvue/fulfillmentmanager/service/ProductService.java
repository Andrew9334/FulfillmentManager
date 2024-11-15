package com.propvue.fulfillmentmanager.service;

import com.propvue.fulfillmentmanager.model.Product;
import com.propvue.fulfillmentmanager.model.ProductStatus;
import com.propvue.fulfillmentmanager.repository.ProductRepository;
import com.propvue.fulfillmentmanager.util.GoogleSheetsUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void loadProductsFromSheet(String spreadsheetId, String range, String accessToken) throws IOException {
        List<List<Object>> rows = GoogleSheetsUtil.getSheetData(spreadsheetId, range, accessToken);
        List<Product> productsBatch = new ArrayList<>();

        for (List<Object> row : rows) {
            Product product = new Product(
                    (String) row.get(0),                             // productId
                    ProductStatus.valueOf((String) row.get(1)),      // статус
                    (String) row.get(2),                             // fulfillmentCenter
                    Integer.parseInt((String) row.get(3)),           // quantity
                    Double.parseDouble((String) row.get(4))          // value
            );
            productsBatch.add(product);

            if (productsBatch.size() >= 1000) {
                productRepository.saveAll(productsBatch);
                productsBatch.clear();
            }
        }

        if (!productsBatch.isEmpty()) {
            productRepository.saveAll(productsBatch);
        }
    }

    public Page<Product> getProductsByStatus(ProductStatus status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable);
    }

    public double calculateTotalValueByStatus(ProductStatus status) {
        return productRepository.sumValueByStatus(status);
    }

    public Page<Product> getProductsByFulfillmentCenter(String fulfillmentCenter, Pageable pageable) {
        return productRepository.findByFulfillmentCenter(fulfillmentCenter, pageable);
    }
}

