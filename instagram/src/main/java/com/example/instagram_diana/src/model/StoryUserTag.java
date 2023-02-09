package com.example.instagram_diana.src.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "storyUserTag")
public class StoryUserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storyUserTagId", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storyMediaId", nullable = false)
    private StroyMedia storyMedia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;


}