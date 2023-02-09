package com.example.instagram_diana.config.oauth;

import com.example.instagram_diana.config.auth.PrincipalDetails;
import com.example.instagram_diana.src.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private final JwtService jwtService;

    // userService에서 던진 principal 정보 받아온다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {



        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentiacation Principal Info: "+principalDetailis);

        System.out.println("onAuthenticationSuccess handeler is executed by jjungie."+principalDetailis.getUser().getId());


        Long userIdx = principalDetailis.getUser().getId();
        //jwt 발급.
        String jwt = jwtService.createJwt(userIdx);

        System.out.println("OAuth2SuccessHandler class Execution:"+jwt);

       // response.addHeader(JwtProperties.HEADER_STRING, jwt);

        //------------ redirect url with jwt token
        String username = principalDetailis.getUser().getUsername();

        //패스워드 입력하도록 리다이렉트
        response.sendRedirect("/app/users/" + username);
        //------------
    }

}
