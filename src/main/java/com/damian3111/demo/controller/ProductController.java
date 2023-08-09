package com.damian3111.demo.controller;

import com.damian3111.demo.ProductDTO;
import com.damian3111.demo.entity.Product;
import com.damian3111.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/getProducts")
    public CompletableFuture<List<Product>> getProduct(@RequestParam("productID") List<Long> listProductID){
        List<CompletableFuture<Product>> list = listProductID.stream()
                .map(productService::getProduct)
                .collect(Collectors.toList());

        CompletableFuture<Void> allOf = CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));
        CompletableFuture<List<Product>> listCompletableFuture = allOf.thenApply(r -> list.stream()
                .map(future -> future.join())
                .collect(Collectors.toList()));
        return listCompletableFuture;
    }

    @GetMapping("/getAllProducts")
    public CompletableFuture<List<Product>> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/saveProduct")
    public CompletableFuture<Product> saveProduct(@RequestBody ProductDTO productDTO){
            return productService.save(productDTO);
    }
}
