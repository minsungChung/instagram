package com.example.instagram_diana.src.member.controller;

import com.example.instagram_diana.src.common.exception.BaseException;
import com.example.instagram_diana.src.common.response.BaseResponse;
import com.example.instagram_diana.src.member.dto.FollowDto;
import com.example.instagram_diana.src.member.service.FollowService;
import com.example.instagram_diana.src.member.service.UserService;
import com.example.instagram_diana.src.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.instagram_diana.src.common.response.BaseResponseStatus.*;

@RequestMapping("/app")
@RequiredArgsConstructor
@RestController
public class FollowController {


    private final FollowService followService;
    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/follows/users/{toUserId}")
    public BaseResponse<?> PostFollow(@PathVariable("toUserId") long toUserId) throws BaseException{
        try{

            if (!userService.checkUserExist(toUserId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }

            Long fromUserId = jwtService.getUserIdx();

            if(toUserId==fromUserId){
                return new BaseResponse<>(USER_ID_CANNOT_FOLLOW);
            }

            if(followService.checkFollow(fromUserId,toUserId)==1){
                return new BaseResponse<>(USER_ID_FOLLOW_EXISTS);
            }else{
                followService.follow(fromUserId,toUserId);
                return new BaseResponse<>("구독을 완료했습니다.");
            }

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/unfollows/users/{toUserId}")
    public BaseResponse<?> PostUnFollow(@PathVariable("toUserId") long toUserId) throws BaseException{
        try{
            if (!userService.checkUserExist(toUserId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }
            Long fromUserId = jwtService.getUserIdx();

            if(toUserId==fromUserId){
                return new BaseResponse<>(USER_ID_CANNOT_FOLLOW);
            }

            if(followService.checkFollow(fromUserId,toUserId)==1){
                followService.unFollow(fromUserId,toUserId);
                return new BaseResponse<>("구독취소를 완료했습니다.");

            }else{
                return new BaseResponse<>(USER_ID_UNFOLLOW_EXISTS);

            }

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/follows/{toUserId}")
    public BaseResponse<?> FollowToggle(@PathVariable("toUserId") long toUserId) throws BaseException {

        if (!userService.checkUserExist(toUserId)){
            return new BaseResponse<>(USER_ID_NOT_EXIST);
        }

        try{
            Long fromUserId = jwtService.getUserIdx();

            if(toUserId==fromUserId){
                return new BaseResponse<>(USER_ID_CANNOT_FOLLOW);
            }

            String returnMessage = (followService.checkFollow(fromUserId,toUserId)==1)?
                    "id:"+ fromUserId + "님이 " + "id:" + toUserId + "님을 구독취소했습니다.":
                    "id:" + fromUserId + "님이 " + "id:"+toUserId + "님을 구독했습니다.";



            if(followService.checkFollow(fromUserId,toUserId)==1){ // 구독안된 상태가 두개: status=inactive,테이블에등록안된유저모두... 테이블관리두개해주어야함ㅇㄴ
                followService.unFollow(fromUserId,toUserId);
            }else{
                followService.follow(fromUserId,toUserId);
            }

            FollowDto followDto = new FollowDto(fromUserId,toUserId);

            return new BaseResponse<>(returnMessage,followDto);
            //  return new ResponseEntity<>(new CustomResponseDto<>(1,returnMessage,null), HttpStatus.OK);
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/follow-states/users/{userId}")
    public BaseResponse<?> followState(@PathVariable("userId") long toUserId){

        if (!userService.checkUserExist(toUserId)){
            return new BaseResponse<>(USER_ID_NOT_EXIST);
        }

        try{
            Long fromUserId = jwtService.getUserIdx();

            if(toUserId==fromUserId){
                return new BaseResponse<>(USER_ID_CANNOT_FOLLOW);
            }

            String returnMessage;
            int isfollow=-1;


            if(followService.checkFollow(fromUserId,toUserId)==1){
                returnMessage =  "id:"+ fromUserId + "님이 " + "id:" + toUserId + "님을 팔로우 한 상태입니다.";
                isfollow=1;
            }else{
                returnMessage =  "id:"+ fromUserId + "님이 " + "id:" + toUserId + "님을 언팔로우 한 상태입니다.";
                isfollow=0;
            }

            return new BaseResponse<>(returnMessage,isfollow);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
