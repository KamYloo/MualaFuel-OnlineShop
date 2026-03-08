package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.ProductDaoImpl;
import com.example.MualaFuel_Backend.dto.CartDto;
import com.example.MualaFuel_Backend.entity.Cart;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.enums.AlcoholType;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock Cart cart;
    @Mock Mapper<Cart, CartDto> cartMapper;
    @Mock
    ProductDaoImpl productDao;

    @InjectMocks CartServiceImpl cartService;

    Product sampleProduct;
    CartDto sampleCartDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProduct = Product.builder()
                .id(1L)
                .name("Test")
                .description("desc")
                .price(BigDecimal.TEN)
                .brand("Brand")
                .alcoholType(AlcoholType.WINE)
                .quantity(5)
                .alcoholContent(12.5)
                .capacityInMilliliters(750)
                .imagePath("img.jpg")
                .build();
        sampleCartDto = new CartDto();
    }

    @Test
    void testAddToCartSuccess() {
        when(productDao.findById(1L)).thenReturn(Optional.of(sampleProduct));

        assertDoesNotThrow(() -> cartService.addToCart(1L, 2));
        verify(cart).addItem(sampleProduct, 2);
    }

    @Test
    void testAddToCartThrowsIfProductNotFound() {
        when(productDao.findById(2L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> cartService.addToCart(2L, 1));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testGetCart() {
        when(cartMapper.mapTo(cart)).thenReturn(sampleCartDto);

        CartDto result = cartService.getCart();

        assertNotNull(result);
        verify(cartMapper).mapTo(cart);
    }

    @Test
    void testUpdateItemQuantity() {
        assertDoesNotThrow(() -> cartService.updateItemQuantity(1L, 3));
        verify(cart).updateQuantity(1L, 3);
    }

    @Test
    void testRemoveItem() {
        assertDoesNotThrow(() -> cartService.removeItem(1L));
        verify(cart).removeItem(1L);
    }
}
