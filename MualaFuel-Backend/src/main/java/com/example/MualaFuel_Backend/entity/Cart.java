package com.example.MualaFuel_Backend.entity;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.*;

@Component
@SessionScope
public class Cart {
    private final Map<Long, CartItem> items = new HashMap<>();

    public void addItem(Product product, int quantity) {
        Long productId = product.getId();

        if (items.containsKey(productId)) {
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(productId, new CartItem(product, quantity));
        }
    }

    public void updateQuantity(Long productId, int newQuantity) {
        CartItem item = items.get(productId);
        if (item == null) {
            throw new CartException("Item not in cart");
        }

        if (newQuantity <= 0) {
            items.remove(productId);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    public void removeItem(Long productId) {
        if(!items.containsKey(productId)) {
            throw new CartException("Item not in cart");
        }
        items.remove(productId);
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
        items.clear();
    }

    public static class CartException extends RuntimeException {
        public CartException(String message) {
            super(message);
        }
    }
}