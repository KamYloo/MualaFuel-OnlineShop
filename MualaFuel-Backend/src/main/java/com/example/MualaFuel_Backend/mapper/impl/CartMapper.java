package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.CartDto;
import com.example.MualaFuel_Backend.entity.Cart;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartMapper implements Mapper<Cart, CartDto> {

    private ModelMapper modelMapper;

    @Override
    public CartDto mapTo(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public Cart mapFrom(CartDto cartDto) {
        return modelMapper.map(cartDto, Cart.class);
    }
}
