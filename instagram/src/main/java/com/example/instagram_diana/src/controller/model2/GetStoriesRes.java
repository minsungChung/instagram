package com.example.instagram_diana.src.controller.model2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoriesRes {
    private Long userId;
    private String profileUrl;
    private String userName;
}
