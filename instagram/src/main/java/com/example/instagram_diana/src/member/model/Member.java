package com.example.instagram_diana.src.member.model;

import com.example.instagram_diana.src.common.BaseEntity;
import lombok.*;

import javax.persistence.*;


@Entity
@Builder
@Table(name = "member")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 300)
    private String password;

    @Column(name = "name", length = 45) //nullable = false
    private String name;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Lob
    @Column(name = "profileUrl")
    private String profileUrl;

    @Column(name = "bio", length = 225)
    private String bio;

    @Column(name = "site", length = 225)
    private String site;

    @Column(name = "phone", length = 45)
    private String phone;

    @Column(name = "provider", nullable = false, length = 45)
    private String provider;

    public void updateName(String name){
        this.name = name;
    }
    public void updateUsername(String username){
        this.username = username;
    }
    public void updateBio(String bio){
        this.bio = bio;
    }
    public void updateSite(String site){
        this.site = site;
    }
    public void updateProfileUrl(String profileUrl){
        this.profileUrl = profileUrl;
    }

    // validation 확인
    public void updateEmail(String email){
        this.email = email;
    }

    public void updatePhone(String phone){
        this.phone = phone;
    }

}