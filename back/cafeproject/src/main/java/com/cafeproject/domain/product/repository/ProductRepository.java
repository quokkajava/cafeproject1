package com.cafeproject.domain.product.repository;

import com.cafeproject.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>  {
    List<Product> findByName(String name);
    List<Product> findByOnSale(boolean onSale);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Product", nativeQuery = true)
    void truncateProductTable();

    Optional<Product> findFirstByOrderByIdDesc();
    Optional<Product> findByIdAndOnSaleTrue(Long id);
}