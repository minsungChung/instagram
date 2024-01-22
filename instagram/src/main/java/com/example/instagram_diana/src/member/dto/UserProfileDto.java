package com.example.instagram_diana.src.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
    private boolean pageOwnerState;
    private int postCount;
    private boolean followState; // 구독한 상태인지(구독=1)
    private int follower;
    private int following;

    private UserProfileUserDto profileUserDto;
    private List<String> ThumbnailUrls;

}
