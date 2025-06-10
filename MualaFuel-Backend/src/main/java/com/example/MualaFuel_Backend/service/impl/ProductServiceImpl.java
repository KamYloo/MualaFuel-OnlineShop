package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.ProductDaoImpl;
import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.FileStorageService;
import com.example.MualaFuel_Backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${application.file.cdn}")
    private String cdn;

    private final ProductDaoImpl productDao;
    private final Mapper<Product, ProductDto> mapper;
    private final FileStorageService fileStorageService;

    @Override
    public ProductDto save(ProductDto product, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String savedRelativePath = fileStorageService.saveFile(image, "products/");
            product.setImagePath(savedRelativePath);
        }

        Product entity = mapper.mapFrom(product);
        Product savedEntity = productDao.save(entity);

        if (savedEntity.getImagePath() != null) {
            savedEntity.setImagePath(cdn + savedEntity.getImagePath());
        }

        return mapper.mapTo(savedEntity);
    }

    @Override
    public ProductDto update(ProductDto productDto, MultipartFile image) {
        Product existing = productDao.findById(productDto.getId())
                .orElseThrow(() -> new CustomException(BusinessErrorCodes.NOT_FOUND));

        Product toUpdate = mapper.mapFrom(productDto);

        if (image != null && !image.isEmpty()) {
            String savedRelativePath = fileStorageService.saveFile(image, "products/");
            toUpdate.setImagePath(savedRelativePath);
        } else {
            toUpdate.setImagePath(existing.getImagePath());
        }

        Product updated = productDao.update(toUpdate);

        if (updated.getImagePath() != null) {
            updated.setImagePath(cdn + updated.getImagePath());
        }

        return mapper.mapTo(updated);
    }


    @Override
    public void delete(long id) {
        productDao.delete(id);
    }

    @Override
    public ProductDto findById(long id) {
        Product product = productDao.findById(id).orElseThrow(
                ()-> new CustomException(BusinessErrorCodes.NOT_FOUND));
        product.setImagePath(
                product.getImagePath() != null ?
                        cdn + product.getImagePath(): null);
        return mapper.mapTo(product);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable, ProductSearchDto productSearch) {
        Page<Product> products = productDao.findAll(pageable, productSearch);
        products.getContent().forEach(product ->
                    product.setImagePath(
                            product.getImagePath() != null ?
                                    cdn + product.getImagePath(): null)
        );
        return products;
    }

    @Override
    public ProductDto updateImage(long id, MultipartFile image) {
        Product product = productDao.findById(id).orElseThrow(
                ()-> new CustomException(BusinessErrorCodes.NOT_FOUND));

        String imagePath = fileStorageService.saveFile(image, "products/");
        product.setImagePath(imagePath);
        Product savedProduct = productDao.save(product);
        savedProduct.setImagePath(cdn + savedProduct.getImagePath());
        return mapper.mapTo(savedProduct);
    }
}
