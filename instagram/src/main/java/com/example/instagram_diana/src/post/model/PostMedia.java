package com.example.instagram_diana.src.post.model;

import com.example.instagram_diana.src.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Builder
@Table(name = "post_media")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostMedia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Lob
    @Column(name = "mediaUrl", nullable = false)
    private String mediaUrl;


    @Column(name = "mediaType", nullable = false, length = 10)
    private String mediaType;


}