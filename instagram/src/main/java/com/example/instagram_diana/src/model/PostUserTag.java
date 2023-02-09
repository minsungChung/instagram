package com.example.instagram_diana.src.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "postUserTag")
public class PostUserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postUserTagId", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;


}