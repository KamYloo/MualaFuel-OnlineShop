package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.CartItemDto;
import com.example.MualaFuel_Backend.entity.CartItem;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartItemMapper implements Mapper<CartItem, CartItemDto> {

    private ModelMapper modelMapper;

    @Override
    public CartItemDto mapTo(CartItem item) {
        return modelMapper.map(item, CartItemDto.class);
    }

    @Override
    public CartItem mapFrom(CartItemDto cartItemDto) {
        return modelMapper.map(cartItemDto, CartItem.class);
    }
}
