package com.example.instagram_diana.src.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLoginReq {

    @NotBlank(message = "필수 입력값입니다.")
    private String loginInput;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

}
