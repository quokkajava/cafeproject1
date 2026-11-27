package com.cafeproject.domain.order.entity;

import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // 주문 저장·삭제 시 주문 상품도 함께 (ERD: ON DELETE CASCADE), 목록에서 빠진 상품은 DB에서도 삭제
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

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

    // 상품 담기 (새 주문, 합치기 모두 사용): 같은 상품이면 수량 증가, 아니면 새 줄 추가 → 총액 다시 계산
    public void addItem(Product product, int quantity) {
        items.stream()
                .filter(item -> item.isSameProduct(product))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.increaseQuantity(quantity),
                        () -> items.add(OrderItem.create(this, product, quantity))
                );
        recalculateTotalPrice();
    }

    // 밖에서 items를 직접 add/remove 하지 못하게 읽기 전용으로 반환 (총액 갱신 누락 방지)
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void recalculateTotalPrice() {
        this.totalPrice = items.stream()
                .mapToInt(OrderItem::getSubtotal)
                .sum();
    }
}
