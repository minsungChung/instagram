package com.example.instagram_diana.src.post.model;

import com.example.instagram_diana.src.common.BaseEntity;
import com.example.instagram_diana.src.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Builder
@Table(name = "post")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Lob
    @Column(name = "content", nullable = true)
    private String content;


    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member user;

    public void uploadContent(String content){
        this.content = content;
    }

}

