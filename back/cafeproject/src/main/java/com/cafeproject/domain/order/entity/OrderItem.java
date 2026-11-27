package com.cafeproject.domain.order.entity;

import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ERD 최종안: #31 댓글 (테이블명 order_items, (order_id, product_id) 유니크)
@Entity
@Table(
        name = "order_items",
        uniqueConstraints = {
                // 같은 주문에 같은 상품은 한 줄만: 또 담으면 새 줄이 아니라 수량 증가
                @UniqueConstraint(name = "uk_order_items_order_product", columnNames = {"order_id", "product_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    // 주문 당시 상품 가격 (#63): 나중에 상품 가격이 바뀌어도 주문 금액은 그대로
    @Column(nullable = false)
    private int unitPrice;

    // Order.addItem()에서만 호출 (package-private)
    static OrderItem create(Order order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.order = order;
        item.product = product;
        item.quantity = quantity;
        item.unitPrice = product.getPrice();
        return item;
    }

    // 합치기: 같은 상품을 또 담으면 수량만 더한다
    void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    // 이 줄의 금액 = 주문 당시 가격 × 수량
    public int getSubtotal() {
        return unitPrice * quantity;
    }

    // 같은 상품인지 비교 (아직 저장 전이라 id가 없을 수 있어 객체 비교도 함께)
    boolean isSameProduct(Product other) {
        if (product == other) return true;
        return product.getId() != null && product.getId().equals(other.getId());
    }
}
