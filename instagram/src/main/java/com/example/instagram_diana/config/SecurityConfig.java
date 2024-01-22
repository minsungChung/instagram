package com.example.instagram_diana.config;

import com.example.instagram_diana.config.oauth.OAuth2SuccessHandler;
import com.example.instagram_diana.config.oauth.PrincipalOauth2UserService;
import com.example.instagram_diana.src.member.repository.UserRepository;
import com.example.instagram_diana.src.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // Secure,preAuthorize annotation 활성화
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CorsConfig corsConfig;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Autowired
    private final OAuth2SuccessHandler successHandler;


    // @Bean: 해당 메소드의 리턴되는 오브젝트를 IOC로 등록 해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

//
//    // jwt설정
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilter(corsFilter)
//                .formLogin().disable()
//                .httpBasic().disable()
//                .apply(new MyCustomDsl())
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/v1/user/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/v1/manager/**")
//                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/v1/admin/**")
//                .access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();
//        return http.build();
//    }
//
//    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//            http
//                    .addFilter(corsConfig.corsFilter())
//                    .addFilter(new JwtAuthenticationFilter(authenticationManager));
//                    //.addFilter(new JwtAuthorizationFilter(authenticationManager));
//        }
//    }


     //기존로그인설정

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                //.loginPage("/loginForm")
                .loginProcessingUrl("/login") //securitylogin 주소가 호출되면 Security가 낚아채 로그인 진행해준다.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .successHandler(successHandler)
                //.loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        return http.build();
    }

//    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//            http
//                    .addFilter(corsConfig.corsFilter())
//                    .addFilter(new JwtAuthenticationFilter(jwtService, authenticationManager))
//                    .addFilter(new JwtAuthorizationFilter(authenticationManager, jwtService,userRepository));
//        }
//    }



}

