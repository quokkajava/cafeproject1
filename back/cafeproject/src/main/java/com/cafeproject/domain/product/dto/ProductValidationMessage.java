package com.cafeproject.domain.product.dto;

public final class ProductValidationMessage {
    public static final String NAME_REQUIRED = "상품명은 필수입니다.";
    public static final String ORIGIN_SIZE = "원산지는 255자 이하여야 합니다.";
    public static final String DESCRIPTION_SIZE = "설명은 255자 이하여야 합니다.";
    public static final String IMAGE_URL_INVALID = "http 또는 https로 시작하는 올바른 URL이어야 합니다.";
    public static final String PRICE_REQUIRED = "가격은 필수입니다.";
    public static final String PRICE_POSITIVE_OR_ZERO = "가격은 0 이상이어야 합니다.";

    private ProductValidationMessage() {}
}