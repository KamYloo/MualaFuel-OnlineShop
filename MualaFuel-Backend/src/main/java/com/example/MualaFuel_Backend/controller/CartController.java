package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.CartDto;
import com.example.MualaFuel_Backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestBody CartRequest request) {
        cartService.addToCart(request.productId(), request.quantity());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/items")
    public ResponseEntity<Void> updateQuantity(@RequestBody CartRequest request) {
        cartService.updateItemQuantity(request.productId(), request.quantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return ResponseEntity.noContent().build();
    }

    public record CartRequest(Long productId, int quantity) {}
}