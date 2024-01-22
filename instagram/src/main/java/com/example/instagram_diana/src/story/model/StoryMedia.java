package com.example.instagram_diana.src.story.model;

import com.example.instagram_diana.src.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "story_media")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoryMedia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storyId", nullable = false)
    private Story story;

    @Lob
    @Column(name = "sourceUrl", nullable = false)
    private String sourceUrl;

    @Column(name = "mediaType", nullable = false, length = 20)
    private String mediaType;

}