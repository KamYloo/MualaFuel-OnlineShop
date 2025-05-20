package com.example.MualaFuel_Backend.dto;

import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDto> orderItems;
    private LocalDate orderDate;
    private ShippingDetails shippingDetails;
    private PaymentDetails paymentDetails;
}