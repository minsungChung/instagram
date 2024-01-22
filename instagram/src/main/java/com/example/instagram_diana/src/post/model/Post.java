package com.example.instagram_diana.src.post.model;

import com.example.instagram_diana.src.member.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
//@ToString(exclude = "User")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", columnDefinition = "INT UNSIGNED not null")
    private Long id;

    // 이부분을 아래로 수정함.
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "userId", nullable = false)
//    private User user;

    @Lob
    @Column(name = "content", nullable = true)
    private String content;

    //추가된부분 ---------------
    @JsonIgnoreProperties({"posts"})
    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    // 2차 추가된 부분

    @OneToMany(mappedBy = "post",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<PostMedia> postMediaList;

    //------------------------

    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt",nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PostMedia> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<PostMedia> postMediaList) {
        this.postMediaList = postMediaList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @PrePersist // DB에 insert되기 직전에 실행됨
    public void createdAt(){
        this.createdAt = LocalDateTime.now();
    }

}

// post가 삭제되면 postMedia도 모두 삭제되어야 함...!