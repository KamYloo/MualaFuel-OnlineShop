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

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/find")
    public Page<Product> find(Pageable pageable, @RequestBody ProductSearchDto productSearch) {
        return productService.getAllProducts(pageable, productSearch);
    }

    @PostMapping("/save")
    public ResponseEntity<Product> save(@RequestBody ProductDto product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Product> update(@RequestBody ProductDto product) {
        return new ResponseEntity<>(productService.update(product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }
}
