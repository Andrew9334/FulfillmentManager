package com.propvue.fulfillmentmanager;

import com.propvue.fulfillmentmanager.model.Product;
import com.propvue.fulfillmentmanager.model.ProductStatus;
import com.propvue.fulfillmentmanager.repository.ProductRepository;
import com.propvue.fulfillmentmanager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProductsByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product("p1", ProductStatus.SELLABLE, "fc1", 100, 50.0);
        List<Product> products = Arrays.asList(product);
        PageImpl<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findByStatus(ProductStatus.SELLABLE, pageable)).thenReturn(page);

        Page<Product> result = productService.getProductsByStatus(ProductStatus.SELLABLE, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("p1", result.getContent().get(0).getProductId());
    }

    @Test
    public void testCalculateTotalValueByStatus() {
        when(productRepository.sumValueByStatus(ProductStatus.SELLABLE)).thenReturn(500.0);

        double totalValue = productService.calculateTotalValueByStatus(ProductStatus.SELLABLE);
        assertEquals(500.0, totalValue);
    }
}
