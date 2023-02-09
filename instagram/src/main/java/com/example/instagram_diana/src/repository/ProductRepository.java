package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
