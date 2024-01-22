package com.example.instagram_diana.src.post.model;


import com.example.instagram_diana.src.common.BaseEntity;
import com.example.instagram_diana.src.member.model.Member;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "post_like")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;


}