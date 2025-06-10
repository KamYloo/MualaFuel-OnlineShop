package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDao extends Dao<Product, Long>{
    Page<Product> findAll(Pageable pageable, ProductSearchDto productSearch);
    Product update(Product product);
}
