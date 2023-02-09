package com.example.instagram_diana.src.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    private String imgUrl;


}
