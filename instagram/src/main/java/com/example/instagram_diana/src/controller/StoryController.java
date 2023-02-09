package com.example.instagram_diana.src.controller;

import com.example.instagram_diana.config.BaseException;
import com.example.instagram_diana.config.BaseResponse;
import com.example.instagram_diana.src.controller.model2.*;
import com.example.instagram_diana.src.service.StoryService;
import com.example.instagram_diana.src.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/app"})
public class StoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final StoryService storyService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final JwtService jwtService;

    public StoryController(StoryService storyService, JwtService jwtService) {
        this.storyService = storyService;
        this.jwtService = jwtService;
    }

    @PostMapping({"/stories"})
    public BaseResponse<PostStoryRes> uploadStory(@RequestBody PostStoryReq postStoryReq) throws BaseException {
        try {
            Long userId = this.jwtService.getUserIdx();
            PostStoryRes postStoryRes = this.storyService.uploadStory(userId, postStoryReq);
            return new BaseResponse(postStoryRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }

    @PatchMapping({"/stories/{storyId}"})
    public BaseResponse<PostStoryRes> deleteStory(@PathVariable Long storyId) throws BaseException {
        try {
            PostStoryRes postStoryRes = this.storyService.deleteStory(storyId);
            return new BaseResponse(postStoryRes);
        } catch (BaseException var3) {
            return new BaseResponse(var3.getStatus());
        }
    }

    @GetMapping({"/stories/{storyId}"})
    public BaseResponse<GetStoryRes> watchStory(@PathVariable Long storyId) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            GetStoryRes getStoryRes = this.storyService.watchStory(me, storyId);
            return new BaseResponse(getStoryRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }

    @GetMapping({"/stories/{storyId}/viewers-num"})
    public BaseResponse<GetStoryViewersNumRes> getStoryViewersNum(@PathVariable Long storyId) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            GetStoryViewersNumRes getStoryViewersNumRes = this.storyService.getStoryViewersNum(me, storyId);
            return new BaseResponse(getStoryViewersNumRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }

    @GetMapping({"/stories/{storyId}/viewers"})
    public BaseResponse<List<GetStoryViewersRes>> getStoryViewers(@PathVariable Long storyId) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            List<GetStoryViewersRes> getStoryViewersRes = this.storyService.getStoryViewers(me, storyId);
            return new BaseResponse(getStoryViewersRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }

    @GetMapping({"/users/{userId}/following-stories"})
    public BaseResponse<List<GetStoriesRes>> getStories(@PathVariable Long userId) throws BaseException {
        try {
            Long me = this.jwtService.getUserIdx();
            List<GetStoriesRes> getStoriesRes = this.storyService.getStories(me, userId);
            return new BaseResponse(getStoriesRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }

    @GetMapping({"/users/{userId}/stories"})
    public BaseResponse<List<Long>> getHavingStories(@PathVariable Long userId) throws BaseException {
        try {
            List<Long> getStories = this.storyService.getStories(userId);
            return new BaseResponse(getStories);
        } catch (BaseException var3) {
            return new BaseResponse(var3.getStatus());
        }
    }
}
