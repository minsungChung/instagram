package com.example.instagram_diana.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// 해당페이지 유저의 구독리스트
@AllArgsConstructor
@Data
public class FollowUserDto {
    private Long userId;
    private String name;
    private String userName;
    private String profileImageUrl;
    private Integer followState;
    private Integer equalUserState; // 나 자신이면 뜨지않는다.
//    private Long pageUserId;

}
