package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Order;

import java.util.List;

public interface OrderDao extends Dao<Order, Long> {
    List<Order> findByUserId(Long id);
}
