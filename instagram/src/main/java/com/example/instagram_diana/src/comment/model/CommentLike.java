package com.example.instagram_diana.src.comment.model;

import com.example.instagram_diana.src.common.BaseEntity;
import com.example.instagram_diana.src.member.model.Member;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "comment_like")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commentId", nullable = false)
    private PostComment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private Member user;

}