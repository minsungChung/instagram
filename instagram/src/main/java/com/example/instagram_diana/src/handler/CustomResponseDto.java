package com.example.instagram_diana.src.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomResponseDto<T> {
    private int code; // 1:성공 2:실패
    private String message;
    private T data;
}
