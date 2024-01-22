package com.example.instagram_diana.src.post.controller;

import com.example.instagram_diana.src.common.exception.BaseException;
import com.example.instagram_diana.src.common.response.BaseResponse;
import com.example.instagram_diana.src.member.service.LikeService;
import com.example.instagram_diana.src.post.service.PostService;
import com.example.instagram_diana.src.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.instagram_diana.src.common.response.BaseResponseStatus.POST_ID_NOT_EXISTS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/users")
public class LikeController
{
    private final LikeService likeService;
    private final JwtService jwtService;

    private final PostService postService;

    @GetMapping("/like-states/{postId}")
    public BaseResponse<?> likeState(@PathVariable("postId") long postId){
        try{
            // 주소의 포스트번호가 없으면 에러
            if (!postService.checkPostExist(postId)){
                return new BaseResponse<>(POST_ID_NOT_EXISTS);
            }

            Long loginUserId = jwtService.getUserIdx();
            String msg;
            int res = -1;

            if(likeService.LikeState(postId,loginUserId).getLikeState()==1){
                msg = "게시물 좋아요 상태입니다.";
                res = 1;
            }
            else{
                msg = "게시물 좋아요를 하지않은 상태입니다.";
                res = 0;

            }

            return new BaseResponse<>(msg,res);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
