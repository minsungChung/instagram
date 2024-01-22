package com.example.instagram_diana.src.member.controller;


import com.example.instagram_diana.config.BaseException;
import com.example.instagram_diana.config.BaseResponse;
import com.example.instagram_diana.config.auth.PrincipalDetails;
import com.example.instagram_diana.src.member.dto.*;
import com.example.instagram_diana.src.handler.CustomValidationException;
import com.example.instagram_diana.src.member.service.FollowService;
import com.example.instagram_diana.src.member.service.UserService;
import com.example.instagram_diana.src.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.instagram_diana.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final UserService userService;
    private final FollowService followService;

    @Autowired
    private final JwtService jwtService;




    public UserController(UserService userService, FollowService followService, JwtService jwtService){
        this.userService = userService;
        this.followService = followService;
        this.jwtService = jwtService;
    }



    // 토큰받기 로직!!!!(세션 필터단 oauth2 로그인 + 일반로그인 + JWT 토큰 구현)
    @GetMapping ("/success")
    public BaseResponse<PrincipalDetails> logInSuccess(Authentication authentication)
    {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println(principalDetails);
        System.out.println("안 . 뇽? 반 가 워 나 는 테 스 트 다.");

        return new BaseResponse<>("로그인을 성공했습니다.",principalDetails);
        //return principalDetails;


    }


    @GetMapping ("/{username}")
    public BaseResponse<PostUserRes> Oauth2Login(@PathVariable("username") String username)
    {
        try{

            PostLoginReq postUserReq = new PostLoginReq(username,"없음");
            PostUserRes postLoginRes = userService.logIn(postUserReq);
            return new BaseResponse<>(postLoginRes);

        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }



    /**
     * 일반 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */

    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody @Validated PostUserReq postUserReq,
                                                BindingResult bindingResult) throws BaseException {

        if(bindingResult.hasErrors()){ // 유효성 검사
            getPostUserResBaseResponse(bindingResult);
        }else {


            // username,email,phone 중복 검사
            if(userService.checkNameDuplicate(postUserReq.getUserName())==true && postUserReq.getUserName()!=null){
                return new BaseResponse<>(POST_USERS_EXISTS_NAME);
            }

            // 이메일 값이존재하고, 값이 중복일때 에러
            if(userService.checkEmailDuplicate(postUserReq.getEmail())==true && postUserReq.getEmail()!=null){
                return new BaseResponse<>(POST_USERS_EXISTS_EMAIL);
            }

            if(userService.checkPhoneDuplicate(postUserReq.getPhone())==true && postUserReq.getPhone()!=null){
                return new BaseResponse<>(POST_USERS_EXISTS_PHONE);
            }


            // email, phone 둘 다 없을시 에러
            if (postUserReq.getEmail() == null && postUserReq.getPhone() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_ID);
            }

            // 회원가입
            try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>("회원가입을 성공했습니다.",postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
        }
        return null;
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostUserRes> logIn(@RequestBody @Validated PostLoginReq postLoginReq,
                                           BindingResult bindingResult)
    {

        if(bindingResult.hasErrors()){ // 유효성 검사
            return getPostUserResBaseResponse(bindingResult);
        }else{


            try{
                PostUserRes postLoginRes = userService.logIn(postLoginReq);
                if (postLoginRes==null){
                    return new BaseResponse<>(FAILED_TO_LOGIN);
                }
                return new BaseResponse<>(postLoginRes);
            } catch (BaseException exception){
                return new BaseResponse<>(exception.getStatus());
            }
        }

    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<PatchUserReq> modifyUserInfo(@PathVariable("userIdx") long userIdx, @RequestBody PatchUserReq patchUserReq){
        try {
            if(!userService.checkUserExist(userIdx)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }

            // jwt에서 idx 추출.
            Long userIdxByJwt = jwtService.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 유저가 보낸 수정 데이터 형식 검증

            // 유저네임이 빈값이면 에러
            if(patchUserReq.getUserName()==null) return new BaseResponse<>(PATCH_USERS_NULL_NAME);

            // 같다면 유저정보 변경
            userService.modifyUserInfo(userIdx,patchUserReq);
            return new BaseResponse<>("회원 정보 수정에 성공했습니다.",patchUserReq);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 유저 프로필 정보 조회
    @GetMapping("/profiles/{pageUserId}")
    public BaseResponse<UserProfileDto> userProfile(@PathVariable long pageUserId){

        try {
            // 주소아이디 유효성 체크
            if(!userService.checkUserExist(pageUserId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }

            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();
            UserProfileDto userProfileDto = userService.UserProfile(pageUserId,loginUserId);
            return new BaseResponse<>(userProfileDto);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // 유저 팔로잉 유저 post list 조회
    @GetMapping("/following-posts")
    public BaseResponse <List<userFollowingPostDto>> userFollowingPosts(){
        try {
            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();
            String res = "userId"+loginUserId + " 님의 유저 팔로잉 포스트 정보 조회";
            List<userFollowingPostDto> dtos = userService.userFollowingPosts(loginUserId);
            return new BaseResponse<>(res,dtos);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping ("/{userId}/following-lists")
    public BaseResponse<List<FollowUserDto>> FollowingList(@PathVariable("userId") long pageUserId){
        try{
            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();

            List<FollowUserDto> followUserDtos = followService.followingList(loginUserId, pageUserId);

            return new BaseResponse<>("구독리스트 조회 성공",followUserDtos);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping ("/{userId}/follower-lists")
    public BaseResponse<List<FollowUserDto>> FollowerList(@PathVariable("userId") long pageUserId){
        try{
            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();

            List<FollowUserDto> followUserDtos = followService.followerList(loginUserId, pageUserId);

            return new BaseResponse<>("구독리스트 조회 성공",followUserDtos);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    // 이메일 수정
    @PatchMapping("/{userId}/emails")
    public BaseResponse<?> changeEmail(@PathVariable("userId") long userId,@RequestBody HashMap<Object,String> hash){
        try{
            // 주소아이디 유효성 체크
            if(!userService.checkUserExist(userId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }
            Long loginUserId = jwtService.getUserIdx();

            if(loginUserId!=userId){
                return new BaseResponse<>(PATCH_CAANOT_EXECUTE);
            }

            boolean err = false;
            String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(hash.get("email"));
            if(!m.matches()) {
                return new BaseResponse<>(PATCH_USERS_INVALID_EMAIL);
            }

            userService.modifyUserEmail(userId,hash.get("email"));
            return new BaseResponse<>("유저 이메일 수정이 완료되었습니다.");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @PatchMapping("/{userId}/phones")
    public BaseResponse<?> changePhoneNumber(@PathVariable("userId") long userId,@RequestBody HashMap<Object,String> hash){
        try{
            // 주소아이디 유효성 체크
            if(!userService.checkUserExist(userId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }
            Long loginUserId = jwtService.getUserIdx();

            if(loginUserId!=userId){
                return new BaseResponse<>(PATCH_CAANOT_EXECUTE);
            }

            String pattern2 = "^\\d{3}-\\d{3,4}-\\d{4}$";
            if(!Pattern.matches(pattern2,hash.get("phone"))) {
                return new BaseResponse<>(PATCH_USERS_INVALID_PHONE);
            }


            userService.modifyUserPhone(userId,hash.get("phone"));
            return new BaseResponse<>("유저 전화번호 수정이 완료되었습니다.");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }



    //유저 프로필 url 등록
    //@RequestParam("profileImageFile") MultipartFile imgFile
    @PostMapping ("/{pageUserId}/profiles")
    public BaseResponse<?> profileImageUpload(@PathVariable("pageUserId") long pageUserId,
                                                                  @RequestBody HashMap<Object,String> hash){
        try{
            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();

            if(pageUserId!=loginUserId){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }

            String resUrl= userService.profileUpload(loginUserId,hash.get("profileImgUrl"));

            return new BaseResponse<>(resUrl);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    // 유효성 검사 함수
    private BaseResponse<PostUserRes> getPostUserResBaseResponse(BindingResult bindingResult) {
        Map<String,String> errorMap = new HashMap<>();

        for(FieldError error:bindingResult.getFieldErrors()){
            errorMap.put(error.getField(),error.getDefaultMessage());
            System.out.println(error.getDefaultMessage());
        }
        throw new CustomValidationException("유효성 검사 실패",errorMap);
    }



}