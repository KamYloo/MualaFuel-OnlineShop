package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.ProductDao;
import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.enums.AlcoholType;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock ProductDao productDao;
    @Mock Mapper<Product, ProductDto> mapper;
    @Mock FileStorageService fileStorageService;
    @Mock MultipartFile multipartFile;

    @InjectMocks ProductServiceImpl productService;

    Product sampleProduct;
    ProductDto sampleProductDto;

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

        org.springframework.test.util.ReflectionTestUtils.setField(productService, "cdn", "cdn/");

        when(mapper.mapTo(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductDto.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .description(p.getDescription())
                    .price(p.getPrice())
                    .brand(p.getBrand())
                    .alcoholType(p.getAlcoholType())
                    .quantity(p.getQuantity())
                    .alcoholContent(p.getAlcoholContent())
                    .capacityInMilliliters(p.getCapacityInMilliliters())
                    .imagePath(p.getImagePath())
                    .build();
        });
    }

    @Test
    void testSaveProductWithImage() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(fileStorageService.saveFile(any(), anyString())).thenReturn("products/img.jpg");
        when(mapper.mapFrom(any(ProductDto.class))).thenReturn(sampleProduct);
        when(productDao.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDto result = productService.save(sampleProductDto, multipartFile);

        assertNotNull(result);
        assertEquals("cdn/img.jpg", result.getImagePath());
        verify(fileStorageService).saveFile(multipartFile, "products/");
        verify(productDao).save(any(Product.class));
    }

    @Test
    void testSaveProductWithoutImage() {
        when(multipartFile.isEmpty()).thenReturn(true);
        when(mapper.mapFrom(any(ProductDto.class))).thenReturn(sampleProduct);
        when(productDao.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDto result = productService.save(sampleProductDto, multipartFile);

        assertNotNull(result);
        assertEquals("cdn/img.jpg", result.getImagePath());
        verify(productDao).save(any(Product.class));
    }

    @Test
    void testUpdateProductWithImage() {
        when(productDao.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(multipartFile.isEmpty()).thenReturn(false);
        when(fileStorageService.saveFile(any(), anyString())).thenReturn("products/img2.jpg");
        when(mapper.mapFrom(any(ProductDto.class))).thenReturn(sampleProduct);
        when(productDao.update(any(Product.class))).thenReturn(sampleProduct);

        ProductDto result = productService.update(sampleProductDto, multipartFile);

        assertNotNull(result);
        assertEquals("cdn/products/img2.jpg", result.getImagePath());
        verify(fileStorageService).saveFile(multipartFile, "products/");
        verify(productDao).update(any(Product.class));
    }

    @Test
    void testUpdateProductThrowsIfNotFound() {
        when(productDao.findById(1L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> productService.update(sampleProductDto, multipartFile));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testDeleteProduct() {
        assertDoesNotThrow(() -> productService.delete(1L));
        verify(productDao).delete(1L);
    }

    @Test
    void testFindByIdReturnsProduct() {
        when(productDao.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductDto result = productService.findById(1L);

        assertNotNull(result);
        assertEquals("cdn/img.jpg", result.getImagePath());
        verify(productDao).findById(1L);
    }

    @Test
    void testFindByIdThrowsIfNotFound() {
        when(productDao.findById(1L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> productService.findById(1L));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testGetAllProducts() {
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        ProductSearchDto searchDto = new ProductSearchDto();
        Pageable pageable = PageRequest.of(0, 10);

        when(productDao.findAll(pageable, searchDto)).thenReturn(page);

        Page<Product> result = productService.getAllProducts(pageable, searchDto);

        assertEquals(1, result.getContent().size());
        assertEquals("cdn/img.jpg", result.getContent().get(0).getImagePath());
        verify(productDao).findAll(pageable, searchDto);
    }

    @Test
    void testUpdateImage() {
        when(productDao.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(fileStorageService.saveFile(any(), anyString())).thenReturn("products/img.jpg");
        when(productDao.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDto result = productService.updateImage(1L, multipartFile);

        assertNotNull(result);
        assertEquals("cdn/products/img.jpg", result.getImagePath());
        verify(fileStorageService).saveFile(multipartFile, "products/");
        verify(productDao).save(any(Product.class));
    }
}
