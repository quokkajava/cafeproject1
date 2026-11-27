package com.cafeproject.domain.product.dto;

import com.cafeproject.domain.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import static com.cafeproject.domain.product.dto.ProductValidationMessage.*;
import static com.cafeproject.domain.product.dto.ProductValidationMessage.IMAGE_URL_INVALID;
import static com.cafeproject.domain.product.dto.ProductValidationMessage.PRICE_POSITIVE_OR_ZERO;
import static com.cafeproject.domain.product.dto.ProductValidationMessage.PRICE_REQUIRED;

public record CustomerProductDTO(
        @NotBlank(message = NAME_REQUIRED)
        String name,

        @Size(max = 255, message = ORIGIN_SIZE)
        String origin,

        @Size(max = 255, message = DESCRIPTION_SIZE)
        String description,

        @URL(regexp = "^https?://.*", message = IMAGE_URL_INVALID)
        String imageUrl,

        @NotNull(message = PRICE_REQUIRED)
        @PositiveOrZero(message = PRICE_POSITIVE_OR_ZERO)
        Integer price
){
    public CustomerProductDTO(Product product) {
        this(
                product.getName(),
                product.getOrigin(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice()
        );
    }
}