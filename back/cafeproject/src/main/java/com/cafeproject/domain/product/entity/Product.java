package com.cafeproject.domain.product.entity;

import com.cafeproject.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ERD 최종안: #31 댓글 (테이블명 products, stocked_at/expires_at 삭제, name 유니크)
// 주문(#129)에서 참조하려고 필드만 먼저 만든 최소 버전입니다. 수정·판매 중지 등 동작은 상품 담당에서 추가합니다.
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String origin;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean onSale;

    // 새 상품 등록: 판매 중(onSale = true)으로 시작
    public static Product create(String name, String origin, String description, String imageUrl, int price) {
        Product product = new Product();
        product.name = name;
        product.origin = origin;
        product.description = description;
        product.imageUrl = imageUrl;
        product.price = price;
        product.onSale = true;
        return product;
    }
}
