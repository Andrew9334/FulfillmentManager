package com.propvue.fulfillmentmanager;

import com.propvue.fulfillmentmanager.controller.ProductController;
import com.propvue.fulfillmentmanager.model.Product;
import com.propvue.fulfillmentmanager.model.ProductStatus;
import com.propvue.fulfillmentmanager.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testLoadProductsFromSheet() throws Exception {
        doNothing().when(productService).loadProductsFromSheet(anyString(), anyString(), anyString());

        mockMvc.perform(post("/products/load-from-sheet")
                        .param("spreadsheetId", "someId")
                        .param("range", "A1:E10")
                        .param("accessToken", "someToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Products loaded from Google Sheets"));
    }

    @Test
    public void testGetProductsByStatus() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product("p1", ProductStatus.SELLABLE, "fc1", 100, 50.0);
        List<Product> products = Arrays.asList(product);
        PageImpl<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productService.getProductsByStatus(ProductStatus.SELLABLE, pageable)).thenReturn(page);

        mockMvc.perform(get("/products/by-status")
                        .param("status", "SELLABLE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productId").value("p1"))
                .andExpect(jsonPath("$.content[0].status").value("SELLABLE"))
                .andExpect(jsonPath("$.content[0].fulfillmentCenter").value("fc1"))
                .andExpect(jsonPath("$.content[0].quantity").value(100))
                .andExpect(jsonPath("$.content[0].value").value(50.0));
    }

    @Test
    public void testGetProductsByFulfillmentCenter() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product("p1", ProductStatus.SELLABLE, "fc1", 100, 50.0);
        List<Product> products = Arrays.asList(product);
        PageImpl<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productService.getProductsByFulfillmentCenter("fc1", pageable)).thenReturn(page);

        mockMvc.perform(get("/products/by-fulfillment-center")
                        .param("fulfillmentCenter", "fc1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productId").value("p1"))
                .andExpect(jsonPath("$.content[0].status").value("SELLABLE"))
                .andExpect(jsonPath("$.content[0].fulfillmentCenter").value("fc1"))
                .andExpect(jsonPath("$.content[0].quantity").value(100))
                .andExpect(jsonPath("$.content[0].value").value(50.0));
    }

    @Test
    public void testGetTotalValueByStatus() throws Exception {
        when(productService.calculateTotalValueByStatus(ProductStatus.SELLABLE)).thenReturn(500.0);

        mockMvc.perform(get("/products/total-value-by-status")
                        .param("status", "SELLABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(500.0));
    }
}