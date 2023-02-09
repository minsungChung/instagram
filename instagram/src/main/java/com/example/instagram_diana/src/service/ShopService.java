package com.example.instagram_diana.src.service;

import com.example.instagram_diana.src.model.Product;
import com.example.instagram_diana.src.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopService {

    private final ProductRepository productRepository;

    public ShopService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void uploadProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public List<Product> products() {
        return productRepository.findAll();
    }
}
