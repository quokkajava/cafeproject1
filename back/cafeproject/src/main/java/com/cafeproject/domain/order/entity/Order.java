package com.cafeproject.domain.order.entity;

import com.cafeproject.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// ERD 최종안: #31 댓글 (payment_method 삭제, 합치기 기준 = email + delivery_date + zipcode + address)
@Entity
@Table(
        name = "orders", // order는 SQL 예약어
        indexes = {
                @Index(name = "idx_orders_email_delivery_date", columnList = "email, delivery_date"),   // 합치기 조회용
                @Index(name = "idx_orders_delivery_date_status", columnList = "delivery_date, status")  // 관리자 조회용
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 10)
    private String zipcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    @Column(nullable = false)
    private int totalPrice;

    // 새 주문 생성: 상태는 ORDERED, 총액은 0으로 시작 (상품을 담으면서 총액 계산)
    public static Order create(String email, String address, String zipcode, LocalDate deliveryDate) {
        Order order = new Order();
        order.email = email;
        order.address = address;
        order.zipcode = zipcode;
        order.deliveryDate = deliveryDate;
        order.status = OrderStatus.ORDERED;
        order.totalPrice = 0;
        return order;
    }
}
