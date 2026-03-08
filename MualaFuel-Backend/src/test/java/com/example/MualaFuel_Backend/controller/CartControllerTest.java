package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.CartDto;
import com.example.MualaFuel_Backend.dto.CartItemDto;
import com.example.MualaFuel_Backend.controller.CartController.CartRequest;
import com.example.MualaFuel_Backend.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    CartService cartService;

    @InjectMocks
    CartController cartController;

    CartDto sampleCartDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CartItemDto item = new CartItemDto();
        item.setProductId(1L);
        item.setProductName("Product");
        item.setQuantity(2);
        item.setPrice(BigDecimal.TEN);
        item.setTotalPrice(BigDecimal.valueOf(20));
        sampleCartDto = new CartDto(List.of(item), BigDecimal.valueOf(20));
    }

    @Test
    void testGetCart() {
        when(cartService.getCart()).thenReturn(sampleCartDto);

        ResponseEntity<CartDto> response = cartController.getCart();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleCartDto, response.getBody());
        verify(cartService).getCart();
    }

    @Test
    void testAddItem() {
        CartRequest request = new CartRequest(1L, 3);

        ResponseEntity<Void> response = cartController.addItem(request);

        assertEquals(204, response.getStatusCodeValue());
        verify(cartService).addToCart(1L, 3);
    }

    @Test
    void testUpdateQuantity() {
        CartRequest request = new CartRequest(2L, 5);

        ResponseEntity<Void> response = cartController.updateQuantity(request);

        assertEquals(204, response.getStatusCodeValue());
        verify(cartService).updateItemQuantity(2L, 5);
    }

    @Test
    void testRemoveItem() {
        ResponseEntity<Void> response = cartController.removeItem(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(cartService).removeItem(1L);
    }
}
