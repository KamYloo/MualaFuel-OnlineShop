package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper implements Mapper<Product, ProductDto> {

    private ModelMapper modelMapper;

    @Override
    public ProductDto mapTo(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public Product mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }
}
