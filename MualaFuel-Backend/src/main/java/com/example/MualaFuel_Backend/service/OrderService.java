package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;

import java.security.Principal;
import java.sql.SQLException;

public interface OrderService {
    OrderDto placeOrder(ShippingDetails shippingDetails,
                        PaymentDetails paymentDetails,
                        Principal principal) throws SQLException;
}
