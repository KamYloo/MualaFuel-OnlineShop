package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.OrderItem;

import java.util.List;

public interface OrderItemDao extends Dao<OrderItem, Long> {
    List<OrderItem> findAll();
    List<OrderItem> findByOrderId(long orderId);
}
