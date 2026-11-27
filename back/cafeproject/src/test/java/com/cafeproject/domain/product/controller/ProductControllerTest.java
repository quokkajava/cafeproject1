package com.cafeproject.domain.product.controller;

import com.cafeproject.domain.product.dto.ProductRequestDTO;
import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService.truncate();

        for (int i = 0; i < 4; i++) {
            ProductRequestDTO testData = new ProductRequestDTO(
                    "name" + i,
                    "콜롬비아산~" +  i,
                    "테스트용 데이터입니다" + i,
                    null,
                    100000,
                    i % 2 == 0
            );
            productService.create(testData);
        }
    }

    @Test
    @DisplayName("고객의 상품 다건 조회와 단건 조회")
    void t1() throws Exception {
        // 다건 조회: onSale=true 인 상품(i=0, 2)만 반환
        mvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("name0", "name2")));

        // 단건 조회: 판매 중인 상품
        Product onSaleProduct = productService.findByName("name0").getFirst();

        mvc.perform(get("/api/products/" + onSaleProduct.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name0"))
                .andExpect(jsonPath("$.origin").value("콜롬비아산~0"))
                .andExpect(jsonPath("$.description").value("테스트용 데이터입니다0"))
                .andExpect(jsonPath("$.price").value(100000));

        // 단건 조회: 판매 중이 아닌 상품은 404
        Product notOnSaleProduct = productService.findByName("name1").getFirst();

        mvc.perform(get("/api/products/" + notOnSaleProduct.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}