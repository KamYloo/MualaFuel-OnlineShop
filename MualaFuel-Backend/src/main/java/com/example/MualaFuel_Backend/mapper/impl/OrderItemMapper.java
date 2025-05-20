package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.OrderItemDto;
import com.example.MualaFuel_Backend.entity.OrderItem;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderItemMapper implements Mapper<OrderItem, OrderItemDto> {

    private ModelMapper modelMapper;

    @Override
    public OrderItemDto mapTo(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }

    @Override
    public OrderItem mapFrom(OrderItemDto orderItemDto) {
        return modelMapper.map(orderItemDto, OrderItem.class);
    }
}
