package com.cafeproject.domain.product.controller;

import com.cafeproject.domain.product.dto.CustomerProductDTO;
import com.cafeproject.domain.product.dto.ProductRequestDTO;
import com.cafeproject.domain.product.dto.ProductValidationMessage;
import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.domain.product.service.ProductService;
import com.cafeproject.global.globalExceptionHandler.BusinessException;
import com.cafeproject.global.globalExceptionHandler.ProductError;
import com.cafeproject.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.cafeproject.domain.product.dto.ProductValidationMessage.*;
import static com.cafeproject.domain.product.dto.ProductValidationMessage.IMAGE_URL_INVALID;
import static com.cafeproject.domain.product.dto.ProductValidationMessage.PRICE_POSITIVE_OR_ZERO;
import static com.cafeproject.domain.product.dto.ProductValidationMessage.PRICE_REQUIRED;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
@Tag(name = "ProductController", description = "customer - Products")
public class ProductController {
    private final ProductService productService;


    @GetMapping
    @Operation(summary = "다건 조회")
    public List<CustomerProductDTO> getAllItems() {
        List<Product> products = productService.findByOnSale(true);
        List<CustomerProductDTO> customerProducts = new ArrayList<>();
        products.forEach(p -> customerProducts.add(new CustomerProductDTO(p)));
        return customerProducts;
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건 조회")
    public CustomerProductDTO getItem(
            @PathVariable long id
    ) {
        return productService.findOnSaleById(id);
    }
}
