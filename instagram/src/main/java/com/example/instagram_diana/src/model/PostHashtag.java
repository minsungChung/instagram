package com.example.instagram_diana.src.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "postHashtag")
public class PostHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(name = "tag", nullable = false, length = 100)
    private String tag;


}