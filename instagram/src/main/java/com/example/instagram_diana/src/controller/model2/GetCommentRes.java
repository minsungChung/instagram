package com.example.instagram_diana.src.controller.model2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentRes {
    private String profileUrl;
    private String username;
    private String content;
    private Long recommentNum;
    private Long likeNum;
    private LocalDateTime updatedAt;
}
