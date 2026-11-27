package com.cafeproject.domain.product.controller;

import com.cafeproject.domain.product.dto.ProductRequestDTO;
import com.cafeproject.domain.product.dto.ProductValidationMessage;
import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.domain.product.service.ProductService;
import com.cafeproject.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/admin/products")
@RestController
@RequiredArgsConstructor
@Tag(name = "AdminProductController", description = "Adㅡin - Products")
public class AdminProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "다건 조회")
    public List<Product> getAllItems() {
        return productService.findAll();
    }

    // GET /api/admin/products?onSale=true
    @GetMapping(params = "onSale")
    @Transactional(readOnly = true)
    @Operation(summary = "다건 조회 + 쿼리 온세일")
    public List<Product> getItems(@RequestParam boolean onSale) {
        return productService.findByOnSale(onSale);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건 조회")
    public Product getItem(
            @PathVariable long id
    ) {
        return productService.findById(id);
    }


    @PostMapping
    @Transactional
    @Operation(summary = "생성")
    public RsData<Product> write(
            @RequestBody @Valid ProductRequestDTO reqBody
    ) {
        Product product = productService.create(reqBody);
        return new RsData<>(
                HttpStatus.CREATED,
                "%d번 상품이 생성되었습니다.".formatted(product.getId()),
                product
        );
    }


    public record PostModifyReqBody(
            @Nullable
            String name,

            @Nullable
            @Size(max = 255, message = ProductValidationMessage.ORIGIN_SIZE)
            String origin,

            @Nullable
            @Size(max = 255, message = ProductValidationMessage.DESCRIPTION_SIZE)
            String description,

            @Nullable
            @URL(regexp = "^https?://.*", message = ProductValidationMessage.IMAGE_URL_INVALID)
            String imageUrl,

            @Nullable
            @PositiveOrZero(message = ProductValidationMessage.PRICE_POSITIVE_OR_ZERO)
            Integer price,

            @Nullable
            Boolean onSale
    ) {
    }
    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RsData<Product> modify(
            @PathVariable long id,
            @RequestBody @Valid PostModifyReqBody reqBody
    ) {
        Product product = productService.findById(id);
        productService.modify(product, reqBody);

        return new RsData<>(
                HttpStatus.OK,
                "%d번 상품이 수정되었습니다.".formatted(product.getId()),
                product
        );
    }


    @PatchMapping("/{id}")
    @Transactional
    @Operation(summary = "onSale 수정")
    public RsData<Void> modify(
            @PathVariable long id,
            @RequestBody boolean onSale
    ) {
        Product product = productService.findById(id);
        productService.modify(product, onSale);

        return new RsData<>(
                HttpStatus.NO_CONTENT,
                "%d번 상품이 수정되었습니다.".formatted(product.getId()),
                null
        );
    }


    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    public RsData<Void> delete(@PathVariable long id) {
        Product post = productService.findById(id);
        productService.delete(post);

        return new RsData<>(
                HttpStatus.NO_CONTENT,
                "%d번 상품이 영구 삭제되었습니다.".formatted(id)
                ,null
        );
    }

}
