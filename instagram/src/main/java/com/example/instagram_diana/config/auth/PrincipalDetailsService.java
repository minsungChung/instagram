package com.example.instagram_diana.config.auth;


import com.example.instagram_diana.src.member.model.User;
import com.example.instagram_diana.src.member.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 실행됨(규칙)
// ※ 주의 : loginForm의 name명 같게 해야함(name="username"!) -다르다면 SecurityConfig에서 usernameParameter("username2") 설정해주어야..
@Service // 서비스 띄우기
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Security Session(내부 Authentication(내부 UserDetails))
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    @Override
    public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {

        System.out.println("로드바이유저네임!"+loginInput);

        User user = new User();
            // 1. 이메일존재시 이메일로 DB에서 유저 찾아오기
            if(userRepository.existsByEmail(loginInput)){
                user = userRepository.findByEmail(loginInput);
            }
            // 2. 유저네임 존재시 유저네임으로 DB에서 유저 찾아오기
            else if(userRepository.existsByUsername(loginInput)){
                user = userRepository.findByUsername(loginInput);
            }
            // 3. 폰 존재시 폰으로 DB에서 유저 찾아오기
            else if(userRepository.existsByPhone(loginInput)){
                user = userRepository.findByPhone(loginInput);
            }
            System.out.println(user+"hi!!!!!!");
            return new PrincipalDetails(user);

}
}
