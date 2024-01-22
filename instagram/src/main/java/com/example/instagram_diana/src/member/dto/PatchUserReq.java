package com.example.instagram_diana.src.member.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class PatchUserReq {

    private String name;

    private String userName;

    private String bio;

    private String link;

    private String profileUrl;
}
