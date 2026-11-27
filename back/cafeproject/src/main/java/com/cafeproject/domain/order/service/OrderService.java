package com.cafeproject.domain.order.service;

import com.cafeproject.domain.order.dto.OrderResponse;
import com.cafeproject.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 내 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String email) {
        return orderRepository.findAllByEmailOrderByDeliveryDateDesc(email).stream()
                .map(OrderResponse::from)
                .toList();
    }

}
