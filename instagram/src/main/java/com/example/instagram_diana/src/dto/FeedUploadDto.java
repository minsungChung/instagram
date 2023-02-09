package com.example.instagram_diana.src.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class FeedUploadDto {
    private String content;
    private String imgUrl;
}
