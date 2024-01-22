package com.example.instagram_diana.src.comment.controller;

import com.example.instagram_diana.src.common.exception.BaseException;
import com.example.instagram_diana.src.common.response.BaseResponse;
import com.example.instagram_diana.src.comment.dto.GetCommentRes;
import com.example.instagram_diana.src.comment.dto.PostCommentLikeRes;
import com.example.instagram_diana.src.comment.dto.PostCommentReq;
import com.example.instagram_diana.src.comment.dto.PostCommentRes;
import com.example.instagram_diana.src.comment.service.CommentService;
import com.example.instagram_diana.src.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/app"})
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentService commentService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtService jwtService;

    @PostMapping({"/posts/{postId}/comment"})
    public BaseResponse<PostCommentRes> uploadComment(@PathVariable Long postId, @RequestBody PostCommentReq postCommentReq) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            PostCommentRes postCommentRes = this.commentService.uploadComment(me, postId, postCommentReq);
            return new BaseResponse(postCommentRes);
        } catch (BaseException var5) {
            return new BaseResponse(var5.getStatus());
        }
    }

    @PatchMapping({"/posts/{postId}/comment"})
    public BaseResponse<PostCommentRes> deleteComment(@PathVariable Long postId) throws BaseException {
        try {
            PostCommentRes postCommentRes = this.commentService.deleteComment(postId);
            return new BaseResponse(postCommentRes);
        } catch (BaseException var3) {
            return new BaseResponse(var3.getStatus());
        }
    }

    @GetMapping({"/posts/{postId}/comments"})
    public BaseResponse<List<GetCommentRes>> getComments(@PathVariable Long postId) throws BaseException {
        try {
            List<GetCommentRes> getCommentRes = this.commentService.getComments(postId);
            return new BaseResponse(getCommentRes);
        } catch (BaseException var3) {
            return new BaseResponse(var3.getStatus());
        }
    }

    @PostMapping({"/posts/{postId}/comments/{commentId}"})
    public BaseResponse<PostCommentRes> uploadReComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody PostCommentReq postCommentReq) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            PostCommentRes postCommentRes = this.commentService.uploadReComment(me, postId, commentId, postCommentReq);
            return new BaseResponse(postCommentRes);
        } catch (BaseException var6) {
            return new BaseResponse(var6.getStatus());
        }
    }

    @GetMapping({"/posts/{postId}/comments/{commentId}"})
    public BaseResponse<List<GetCommentRes>> getReComments(@PathVariable Long postId, @PathVariable Long commentId) throws BaseException {
        try {
            List<GetCommentRes> getCommentRes = this.commentService.getReComments(postId, commentId);
            return new BaseResponse(getCommentRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }

    @PostMapping({"/posts/{postId}/comments/{commentId}/likes"})
    public BaseResponse<PostCommentLikeRes> uploadCommentLike(@PathVariable Long commentId) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            PostCommentLikeRes postCommentLikeRes = this.commentService.uploadCommentLike(me, commentId);
            return new BaseResponse(postCommentLikeRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }
}
