package com.cafeproject.domain.order.controller;

import com.cafeproject.domain.order.dto.OrderResponse;
import com.cafeproject.domain.order.service.OrderService;
import com.cafeproject.global.rsData.RsData;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 내 주문 목록 조회 : GET/api/orders?email=
    @GetMapping
    public RsData<List<OrderResponse>>
    getMyOrders(@RequestParam @Email String email) {
        return new RsData<>(HttpStatus.OK, "내 주문 목록 조회 성공",
                orderService.getMyOrders(email));
    }
}
