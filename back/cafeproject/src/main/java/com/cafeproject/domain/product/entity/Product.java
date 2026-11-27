package com.cafeproject.domain.product.entity;

import com.cafeproject.domain.product.dto.ProductRequestDTO;
import com.cafeproject.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = true, length = 255)
    private String origin;

    @Column(nullable = true, length = 255)
    private String description;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private int price;

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean onSale = true;

    public Product(ProductRequestDTO dto) {
        this.name = dto.name();
        this.origin = dto.origin();
        this.description = dto.description();
        this.imageUrl = dto.imageUrl();
        this.price = dto.price();
        this.onSale = dto.onSale() == null ? true : dto.onSale();
    }
}
