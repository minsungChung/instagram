package com.example.instagram_diana.src.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class HighlightMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mappingtId", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storyId", nullable = false)
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "highlightId", nullable = false)
    private StoryHighlight highlight;


}