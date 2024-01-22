package com.example.instagram_diana.src.story.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoryRes {
    private String profileUrl;
    private String username;
    private String mediaUrl;
    private Long postId;
    private LocalDateTime updatedAt;
}
