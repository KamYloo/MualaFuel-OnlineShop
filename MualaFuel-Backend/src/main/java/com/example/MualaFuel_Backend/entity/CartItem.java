package com.example.MualaFuel_Backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CartItem {
    private final Long productId;
    private final String productName;
    private final BigDecimal price;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    public void updateQuantity(int quantityChange) {
        int newQuantity = this.quantity + quantityChange;
        if(newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be zero or negative");
        }
        this.quantity = newQuantity;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
