package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.ProductDao;
import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final Mapper<Product, ProductDto> mapper;

    @Override
    public Product save(ProductDto product) {
        return productDao.save(mapper.mapFrom(product));
    }

    @Override
    public Product update(ProductDto product) {
        return productDao.update(mapper.mapFrom(product));
    }

    @Override
    public void delete(long id) {
        productDao.delete(id);
    }

    @Override
    public Product findById(long id) {
        return productDao.findById(id).orElseThrow(
                ()-> new CustomException(BusinessErrorCodes.NOT_FOUND));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable, ProductSearchDto productSearch) {
        return productDao.findAll(pageable, productSearch);
    }
}
