package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;
import jakarta.mail.MessagingException;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto placeOrder(ShippingDetails shippingDetails,
                        PaymentDetails paymentDetails,
                        Principal principal) throws MessagingException, SQLException;
    List<OrderDto> getAllOrdersOfUser(Principal principal);
    void updateStatusOfOrder(Long orderId);
    void cancelOrder(Long orderId);
}
