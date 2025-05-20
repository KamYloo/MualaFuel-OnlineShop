package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.ProductDao;
import com.example.MualaFuel_Backend.dto.CartDto;
import com.example.MualaFuel_Backend.entity.Cart;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final Cart cart;
    private final Mapper<Cart, CartDto> cartMapper;
    private final ProductDao productRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NOT_FOUND));
        cart.addItem(product, quantity);
    }

    public CartDto getCart() {
        return cartMapper.mapTo(cart);
    }

    @Override
    public void updateItemQuantity(Long productId, int newQuantity) {
        cart.updateQuantity(productId, newQuantity);
    }

    @Override
    public void removeItem(Long productId) {
        cart.removeItem(productId);
    }

}
