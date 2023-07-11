package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(value = "id")UUID id){
        Optional<ProductModel> productModelOptional = productRepository.findById(id);
        if (productModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(productModelOptional.get());
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProductById(@PathVariable(value = "id")UUID id,
                                                    @RequestBody @Valid ProductRecordDto productRecordDto){
        Optional<ProductModel> productModelOptional = productRepository.findById(id);
        if (productModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var productModel = productModelOptional.get();
        BeanUtils.copyProperties(productRecordDto,productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<List<ProductModel>> deleteProductById(@PathVariable(value = "id")UUID id){
        Optional<ProductModel> productModelOptional = productRepository.findById(id);
        if (productModelOptional.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        ProductModel productToDelete = productModelOptional.get();
        productRepository.delete(productToDelete);

        List<ProductModel> productList = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }


}
