package com.example.instagram_diana.src.comment.model;

import com.example.instagram_diana.src.common.BaseEntity;
import com.example.instagram_diana.src.member.model.Member;

import com.example.instagram_diana.src.post.model.Post;
import lombok.*;


import javax.persistence.*;


@Entity
@Builder
@Table(name = "post_comment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private Member user;

    @Column(name = "content", nullable = false, length = 225)
    private String content;

    @Column(name = "parentId", columnDefinition = "INT UNSIGNED not null")
    private Long parentId;

}