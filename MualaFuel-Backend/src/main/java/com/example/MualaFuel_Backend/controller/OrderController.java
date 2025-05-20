package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.dto.OrderRequest;
import com.example.MualaFuel_Backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.SQLException;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(
            @RequestBody OrderRequest request,
            Principal principal
    ) throws SQLException {
        OrderDto order = orderService.placeOrder(
                request.shippingDetails(),
                request.paymentDetails(),
                principal
        );
        return ResponseEntity.ok(order);
    }
}
