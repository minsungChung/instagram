package com.example.instagram_diana.src.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowDto {
    private long fromUserId;
    private long toUserId;
//    private Integer followState;
//    private Integer equalUserState;
}
