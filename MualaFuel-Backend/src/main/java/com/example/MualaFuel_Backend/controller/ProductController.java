package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.ProductDto;
import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/find")
    public Page<Product> find(Pageable pageable, @RequestBody ProductSearchDto productSearch) {
        return productService.getAllProducts(pageable, productSearch);
    }

    @PostMapping("/save")
    public ResponseEntity<ProductDto> save(@ModelAttribute ProductDto productDto,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(productService.save(productDto, image), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> update(@ModelAttribute ProductDto product,
                                             @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(productService.update(product, image), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/update/image/{id}")
    public ResponseEntity<ProductDto> updateImage(@PathVariable long id, @RequestParam("image") MultipartFile image) {
        return new ResponseEntity<>(productService.updateImage(id, image), HttpStatus.OK);
    }
}
