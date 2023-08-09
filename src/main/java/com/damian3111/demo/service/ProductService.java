package com.damian3111.demo.service;

import com.damian3111.demo.ProductDTO;
import com.damian3111.demo.entity.Product;
import com.damian3111.demo.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class ProductService {
    private final Random random = new Random();
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CompletableFuture<Product> save(ProductDTO productDTO) {
        doSomething();
        return CompletableFuture.supplyAsync(() -> {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            return productRepository.save(product);
        });
    }

    public CompletableFuture<Product> getProduct(long productId) {
        return CompletableFuture.supplyAsync(() -> {

            log.info(Thread.currentThread().getName());
            doSomething();
            return productRepository.findById(productId).orElseThrow();
        });
    }

    @Async
    public CompletableFuture<List<Product>> getAllProducts() {
        log.info(Thread.currentThread().getName());
        doSomething();
        return CompletableFuture.completedFuture(productRepository.findAll());
    }

    public void doSomething() {
        try {
            Thread.sleep(random.nextInt(1000, 5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
