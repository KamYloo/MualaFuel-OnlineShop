package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderMapper implements Mapper<Order, OrderDto> {

    private ModelMapper modelMapper;

    @Override
    public OrderDto mapTo(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Order mapFrom(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }
}
