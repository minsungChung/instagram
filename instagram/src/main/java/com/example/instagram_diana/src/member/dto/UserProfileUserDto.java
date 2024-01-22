package com.example.instagram_diana.src.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
public class UserProfileUserDto {
    private String username;
    private String name;
    private String bio;
    private String profileUrl;
    private String site;

    @Builder
    public UserProfileUserDto(String username,String name,String bio,String profileUrl,String site){
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.profileUrl = profileUrl;
        this.site = site;
    }

}
