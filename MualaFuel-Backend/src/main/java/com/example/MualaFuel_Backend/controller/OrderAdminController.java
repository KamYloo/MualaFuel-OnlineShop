package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderService orderService;

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id) throws SQLException {

        orderService.updateStatusOfOrder(id);
        return ResponseEntity.ok("Order status updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) throws SQLException {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled");
    }

}
