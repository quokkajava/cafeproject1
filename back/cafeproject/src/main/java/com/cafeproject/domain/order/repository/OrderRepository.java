package com.cafeproject.domain.order.repository;

import com.cafeproject.domain.order.entity.Order;
import com.cafeproject.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주문 합치기용 (#31): 이메일 + 배송일 + 우편번호 + 주소가 모두 같고, 해당 상태인 주문
    // 서비스에서는 status = ORDERED로 호출 → 이미 준비·배송 중이거나 취소된 주문에는 합치지 않음 (#76 팀 결정에 따라 조정)
    @Query("""
            SELECT o FROM Order o
            WHERE o.email = :email
              AND o.deliveryDate = :deliveryDate
              AND o.zipcode = :zipcode
              AND o.address = :address
              AND o.status = :status
            """)
    Optional<Order> findMergeableOrder(
            String email, LocalDate deliveryDate, String zipcode, String address, OrderStatus status);

    // 내 주문 조회용 (#127): 이메일로 찾고 배송일 최신순
    List<Order> findAllByEmailOrderByDeliveryDateDesc(String email);
}
