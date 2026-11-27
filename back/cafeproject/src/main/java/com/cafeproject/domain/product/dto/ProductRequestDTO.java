package com.cafeproject.domain.product.dto;

import com.cafeproject.domain.product.entity.Product;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import static com.cafeproject.domain.product.dto.ProductValidationMessage.*;

public record ProductRequestDTO(
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
        Integer price,

        Boolean onSale
) {
    public ProductRequestDTO(Product product) {
        this(
                product.getName(),
                product.getOrigin(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getOnSale() == null ? true : product.getOnSale()
        );
    }
}
