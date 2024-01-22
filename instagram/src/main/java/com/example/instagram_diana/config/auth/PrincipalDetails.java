package com.example.instagram_diana.config.auth;



// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행완료시 Security Session을 만들어준다. (Security ContextHolder)
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session영역 => Authentication 객체 => UserDetails 타입객체 (implements UserDetails)

import com.example.instagram_diana.src.member.model.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member user;
    private Map<String,Object> attributes;

    // 생성자

    // 일반 로그인
    public PrincipalDetails(Member user) {
        this.user = user;
    }

    // OAut 로그인
    public PrincipalDetails(Member user, Map<String,Object> attributes) {

        this.user = user;
        this.attributes = attributes;
    }


    // Map
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    // 해당 User의 ACTIVE권한을 리턴하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collect = new ArrayList<>();
//        collect.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getStatus();
//            }
//        });
//        return collect;
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // true설정
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 1년동안 회원로그인X시 휴먼계정
        // 현재시간 - 로긴시간 => 1년초과시 return false;
        return true;
    }


}
