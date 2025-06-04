package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDto save(ProductDto product, MultipartFile image);
    ProductDto update(ProductDto product, MultipartFile image);
    void delete(long id);
    ProductDto findById(long id);
    Page<Product> getAllProducts(Pageable pageable, ProductSearchDto productSearch);
    ProductDto updateImage(long id, MultipartFile image);
}
