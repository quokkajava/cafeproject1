package com.cafeproject.domain.order.entity;

//ERD의 ORDERS TABLE 참조

public enum OrderStatus {
    ORDERED,     // 주문 접수
    PREPARING,   // 상품 준비 중
    SHIPPING,    // 배송 중
    DELIVERED,   // 배송 완료
    CANCELED     // 취소
}
