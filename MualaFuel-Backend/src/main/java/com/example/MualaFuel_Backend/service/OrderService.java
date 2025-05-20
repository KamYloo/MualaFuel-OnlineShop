package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    OrderDto placeOrder(ShippingDetails shippingDetails,
                        PaymentDetails paymentDetails,
                        Principal principal) throws SQLException;
    List<OrderDto> getAllOrdersOfUser(Principal principal) throws SQLException;
    void updateStatusOfOrder(Long orderId) throws SQLException;
    void cancelOrder(Long orderId) throws SQLException;
}
