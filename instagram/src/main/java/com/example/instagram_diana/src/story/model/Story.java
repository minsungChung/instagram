package com.example.instagram_diana.src.story.model;

import com.example.instagram_diana.src.common.BaseEntity;
import com.example.instagram_diana.src.member.model.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "story")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Story extends BaseEntity {

    @Column(name = "contentType", length = 20)
    private String contentType;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "postId")
    private Long postId;

    @Lob
    @Column(name = "mediaUrl")
    private String mediaUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private Member user;

}