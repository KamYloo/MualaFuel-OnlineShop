package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product save(ProductDto product);
    Product update(ProductDto product);
    void delete(long id);
    Product findById(long id);
    Page<Product> getAllProducts(Pageable pageable, ProductSearchDto productSearch);
}
