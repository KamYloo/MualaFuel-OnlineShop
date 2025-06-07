package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.dto.OrderRequest;
import com.example.MualaFuel_Backend.service.OrderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(
            @RequestBody OrderRequest request,
            Principal principal
    ) throws SQLException, MessagingException {
        OrderDto order = orderService.placeOrder(
                request.shippingDetails(),
                request.paymentDetails(),
                principal
        );
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(Principal principal) throws SQLException {
        List<OrderDto> list = orderService.getAllOrdersOfUser(principal);
        return ResponseEntity.ok(list);
    }


}
