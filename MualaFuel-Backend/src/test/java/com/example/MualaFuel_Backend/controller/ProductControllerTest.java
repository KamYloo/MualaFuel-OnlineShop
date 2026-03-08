package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.enums.AlcoholType;
import com.example.MualaFuel_Backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    ProductDto sampleProductDto;
    Product sampleProduct;
    ProductSearchDto searchDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProductDto = ProductDto.builder()
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

        searchDto = ProductSearchDto.builder().name("Test").build();
    }

    @Test
    void testFind() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        when(productService.getAllProducts(pageable, searchDto)).thenReturn(page);

        Page<Product> result = productController.find(pageable, searchDto);

        assertEquals(1, result.getTotalElements());
        assertEquals(sampleProduct, result.getContent().get(0));
        verify(productService).getAllProducts(pageable, searchDto);
    }

    @Test
    void testSave() {
        MultipartFile image = mock(MultipartFile.class);
        when(productService.save(sampleProductDto, image)).thenReturn(sampleProductDto);

        ResponseEntity<ProductDto> response = productController.save(sampleProductDto, image);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleProductDto, response.getBody());
        verify(productService).save(sampleProductDto, image);
    }

    @Test
    void testUpdate() {
        MultipartFile image = mock(MultipartFile.class);
        when(productService.update(sampleProductDto, image)).thenReturn(sampleProductDto);

        ResponseEntity<ProductDto> response = productController.update(sampleProductDto, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleProductDto, response.getBody());
        verify(productService).update(sampleProductDto, image);
    }

    @Test
    void testDelete() {
        doNothing().when(productService).delete(1L);

        ResponseEntity<?> response = productController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody());
        verify(productService).delete(1L);
    }

    @Test
    void testFindById() {
        when(productService.findById(1L)).thenReturn(sampleProductDto);

        ResponseEntity<ProductDto> response = productController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleProductDto, response.getBody());
        verify(productService).findById(1L);
    }

    @Test
    void testUpdateImage() {
        MultipartFile image = mock(MultipartFile.class);
        when(productService.updateImage(1L, image)).thenReturn(sampleProductDto);

        ResponseEntity<ProductDto> response = productController.updateImage(1L, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleProductDto, response.getBody());
        verify(productService).updateImage(1L, image);
    }
}
