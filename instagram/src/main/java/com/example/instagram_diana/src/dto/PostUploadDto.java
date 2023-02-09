package com.example.instagram_diana.src.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUploadDto {
    private Long userId;
    private String content;
    private List<String> imgUrlList;
}
