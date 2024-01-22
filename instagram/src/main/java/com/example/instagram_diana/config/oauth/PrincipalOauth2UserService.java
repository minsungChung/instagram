package com.example.instagram_diana.config.oauth;

import com.example.instagram_diana.config.auth.PrincipalDetails;
import com.example.instagram_diana.config.secret.Secret;
import com.example.instagram_diana.src.member.model.Member;
import com.example.instagram_diana.src.member.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;


// 유저 정보 가져오기
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService  {

    @Autowired
    private UserRepository userRepository;


    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 회원가입 진행
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){

            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());

        }

        else if(userRequest.getClientRegistration().getRegistrationId().equals("google")){

            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        }
        else{
            System.out.println("연동로그인은 페이스북만 지원합니다.");
        }

        String provider = oAuth2UserInfo.getProvider(); // facebook
        String providerId = oAuth2UserInfo.getProviderId();


        String username = provider+"-"+providerId; // facebook_103234234234534543534
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        System.out.println("userService execute.");
        System.out.println(provider);
        System.out.println(providerId);

        System.out.println(username);
        //System.out.println(password);
        System.out.println(email);
        System.out.println(role);


        // 회원가입 (DB저장)

        Member userEntity = userRepository.findByUsername(username);
        if(userEntity == null){        // 이미 회원가입이 되어있을때 에러
            userEntity = Member.builder()
                    .username(username)
                    .name(null)
                    .email(email)
                    .phone(null)
                    .password("oauth2") //new SHA256().encrypt("페이스북패스워드")
                    .provider(provider)
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("로그인을 이미 한 적이 있습니다.");
        }

//========================================================================
        PrincipalDetails principalDetails  = new PrincipalDetails(userEntity);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                        null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                        principalDetails.getAuthorities());

        // 강제로 시큐리티의 세션에 접근하여 값 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String jwtToken = Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userEntity.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();

        System.out.println("hi jwtToken by oauth:"+jwtToken);

    //==================================================================


        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
        //return new BaseResponse<>("OAuth2User 회원가입을 성공했습니다.",userEntity);

    }
}
