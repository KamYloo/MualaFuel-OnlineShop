package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.CartDto;

public interface CartService {
    void addToCart(Long productId, int quantity);
    CartDto getCart();
    void updateItemQuantity(Long productId, int newQuantity);
    void removeItem(Long productId);
}
