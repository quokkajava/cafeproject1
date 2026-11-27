package com.cafeproject.domain.product.controller;


import com.cafeproject.domain.product.dto.ProductRequestDTO;
import com.cafeproject.domain.product.dto.ProductValidationMessage;
import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.domain.product.service.ProductService;
import com.cafeproject.global.globalExceptionHandler.CommonError;
import com.cafeproject.global.globalExceptionHandler.ProductError;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminProductControllerTest {
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
                    true
            );
            productService.create(testData);
        }
    }

    @Test
    @DisplayName("상품 생성")
    void t1() throws Exception {
        ResultActions resultActions = mvc.perform(
          post("/api/admin/products")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("""
                          {
                            "name": "new coffee",
                            "origin": "new origin",
                            "description": "new description",
                            "imageUrl": "https://placehold.co/600x400/orange/white",
                            "price": 2000000,
                            "onSale": true
                          }
                          """)
        ).andDo(print());

        Product product = productService.findLatest();

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(product.getId()))
                .andExpect(jsonPath("$.data.name").value("new coffee"))
                .andExpect(jsonPath("$.data.description").value("new description"))
                .andExpect(jsonPath("$.data.price").value(2000000))
                .andExpect(jsonPath("$.data.origin").value("new origin"))
                .andExpect(jsonPath("$.data.onSale").value(true));
    }

    @Test
    @DisplayName("상품 생성, without imageurl, onsale")
    void t2() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                          {
                            "name": "new coffee",
                            "origin": "new origin",
                            "description": "new description",
                            "price": 2000000
                          }
                          """)
        ).andDo(print());

        Product product = productService.findLatest();

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(product.getId()))
                .andExpect(jsonPath("$.data.name").value("new coffee"))
                .andExpect(jsonPath("$.data.description").value("new description"))
                .andExpect(jsonPath("$.data.price").value(2000000))
                .andExpect(jsonPath("$.data.origin").value("new origin"))
                .andExpect(jsonPath("$.data.onSale").value(true));
    }

    @Test
    @DisplayName("상품 생성, without price - 400 에러")
    void t3() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                          {
                            "name": "new coffee",
                            "origin": "new origin",
                            "description": "new description"
                          }
                          """)
        ).andDo(print());

        CommonError error = CommonError.INVALID_INPUT_VALUE;

        resultActions.andExpect(status().is(error.getStatus().value()))
                .andExpect(jsonPath("$.statusCode").value(error.getStatus().value()))
                .andExpect(jsonPath("$.code").value(error.getCode()))
                .andExpect(jsonPath("$.message").value(error.getMessage()))
                .andExpect(jsonPath("$.data[0].field").value("price"))
                .andExpect(jsonPath("$.data[0].reason").value(ProductValidationMessage.PRICE_REQUIRED));

        assertEquals(4, productService.count());
    }

    @Test
    @DisplayName("상품 생성, without name - 400 에러")
    void t4() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                          {
                            "origin": "new origin",
                            "description": "new description",
                            "price": 2000000
                          }
                          """)
        ).andDo(print());

        CommonError error = CommonError.INVALID_INPUT_VALUE;

        resultActions.andExpect(status().is(error.getStatus().value()))
                .andExpect(jsonPath("$.statusCode").value(error.getStatus().value()))
                .andExpect(jsonPath("$.code").value(error.getCode()))
                .andExpect(jsonPath("$.message").value(error.getMessage()))
                .andExpect(jsonPath("$.data[0].field").value("name"))
                .andExpect(jsonPath("$.data[0].reason").value(ProductValidationMessage.NAME_REQUIRED));

        assertEquals(4, productService.count());
    }

    @Test
    @DisplayName("상품 다건 조회")
    void t5() throws Exception {
        ResultActions resultActions = mvc.perform(
                get("/api/admin/products")
        ).andDo(print());

        List<Product> products = productService.findAll();

        resultActions.andExpect(status().isOk())
                .andExpect(handler().handlerType(AdminProductController.class))
                .andExpect(handler().methodName("getAllItems"))
                .andExpect(jsonPath("$.length()").value(products.size()));

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(product.getId()))
                    .andExpect(jsonPath("$[%d].name".formatted(i)).value(product.getName()))
                    .andExpect(jsonPath("$[%d].origin".formatted(i)).value(product.getOrigin()))
                    .andExpect(jsonPath("$[%d].description".formatted(i)).value(product.getDescription()))
                    .andExpect(jsonPath("$[%d].price".formatted(i)).value(product.getPrice()))
                    .andExpect(jsonPath("$[%d].onSale".formatted(i)).value(product.getOnSale()));
        }
    }

    @Test
    @DisplayName("상품 다건 조회, onSale=false 필터")
    void t6() throws Exception {
        Product offSaleProduct = productService.findLatest();
        productService.modify(offSaleProduct, false);

        ResultActions resultActions = mvc.perform(
                get("/api/admin/products")
                        .param("onSale", "false")
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(handler().handlerType(AdminProductController.class))
                .andExpect(handler().methodName("getItems"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(offSaleProduct.getId()))
                .andExpect(jsonPath("$[0].onSale").value(false));
    }

    @Test
    @DisplayName("상품 단건 조회")
    void t7() throws Exception {
        Product product = productService.findLatest();

        ResultActions resultActions = mvc.perform(
                get("/api/admin/products/%d".formatted(product.getId()))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(handler().handlerType(AdminProductController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.origin").value(product.getOrigin()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.onSale").value(product.getOnSale()));
    }

    @Test
    @DisplayName("상품 단건 조회, 없는 id - 404 에러")
    void t8() throws Exception {
        long notExistId = productService.findLatest().getId() + 1;

        ResultActions resultActions = mvc.perform(
                get("/api/admin/products/%d".formatted(notExistId))
        ).andDo(print());

        ProductError error = ProductError.NOT_FOUND;

        resultActions.andExpect(status().is(error.getStatus().value()))
                .andExpect(jsonPath("$.statusCode").value(error.getStatus().value()))
                .andExpect(jsonPath("$.code").value(error.getCode()))
                .andExpect(jsonPath("$.message").value(error.getMessage()));
    }
}