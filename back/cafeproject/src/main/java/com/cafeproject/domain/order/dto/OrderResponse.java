package com.cafeproject.domain.order.dto;

import com.cafeproject.domain.order.entity.Order;
import com.cafeproject.domain.order.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 명세서 #21 2-1 응답 형식 (#31 결정으로 paymentMethod 제외)
// merged는 주문 생성 응답에만 포함, 조회 API에서는 null → JSON에서 빠짐
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponse(
        Long orderId,
        Boolean merged,
        String email,
        String address,
        String zipcode,
        OrderStatus status,
        LocalDate deliveryDate,
        int totalPrice,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // 주문 생성 API용: 합쳐졌는지(merged) 함께 응답
    public static OrderResponse of(Order order, boolean merged) {
        return create(order, merged);
    }

    // 조회 API용 (#127, #128): merged 없이 응답
    public static OrderResponse from(Order order) {
        return create(order, null);
    }

    private static OrderResponse create(Order order, Boolean merged) {
        return new OrderResponse(
                order.getId(),
                merged,
                order.getEmail(),
                order.getAddress(),
                order.getZipcode(),
                order.getStatus(),
                order.getDeliveryDate(),
                order.getTotalPrice(),
                order.getItems().stream()
                        .map(OrderItemResponse::from)
                        .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
