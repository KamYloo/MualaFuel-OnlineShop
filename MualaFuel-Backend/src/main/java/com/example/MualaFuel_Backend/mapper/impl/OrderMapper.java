package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.dto.OrderItemDto;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.entity.OrderItem;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper implements Mapper<Order, OrderDto> {

    private ModelMapper modelMapper;
    private final Mapper<OrderItem, OrderItemDto> orderItemMapper;

    @Override
    public OrderDto mapTo(Order order) {
        return modelMapper.map(order, OrderDto.class);
//        OrderDto dto = modelMapper.map(order, OrderDto.class);
//        System.out.println(order.getOrderItems());
//        if (order.getOrderItems() != null) {
//            dto.setOrderItems(
//                    order.getOrderItems().stream()
//                            .map(orderItemMapper::mapTo)
//                            .collect(Collectors.toList())
//            );
//        }
//
//        return dto;
    }

    @Override
    public Order mapFrom(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
//        Order order = modelMapper.map(orderDto, Order.class);
//
//        if (orderDto.getOrderItems() != null) {
//            order.setOrderItems(
//                    orderDto.getOrderItems().stream()
//                            .map(orderItemMapper::mapFrom)
//                            .collect(Collectors.toList())
//            );
//        }
//
//        return order;
    }
}
