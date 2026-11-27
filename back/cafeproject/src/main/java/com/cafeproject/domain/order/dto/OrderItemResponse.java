package com.cafeproject.domain.order.dto;

import com.cafeproject.domain.order.entity.OrderItem;

// 명세서 #21 2-1 응답의 items[] 한 줄
public record OrderItemResponse(
        Long orderItemId,
        Long productId,
        String productName,
        int quantity,
        int unitPrice   // 주문 당시 가격 (현재 상품 가격 아님)
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}
