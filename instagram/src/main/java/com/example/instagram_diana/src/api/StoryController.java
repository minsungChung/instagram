//package com.example.instagram_diana.src.api;
//
//import com.example.instagram_diana.config.BaseException;
//import com.example.instagram_diana.config.BaseResponse;
//import com.example.instagram_diana.src.dto.PostUploadDto;
//import com.example.instagram_diana.src.dto.StoryUploadDto;
//import com.example.instagram_diana.src.service.StoryService;
//import com.example.instagram_diana.src.utils.JwtService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/stories")
//public class StoryController {
//
//    private final JwtService jwtService;
//    private final StoryService storyService;
//
//    public StoryController(JwtService jwtService, StoryService storyService) {
//        this.jwtService = jwtService;
//        this.storyService = storyService;
//    }
//
//    @PostMapping("")
//    public BaseResponse<?> storyUpload(@RequestBody StoryUploadDto storyUploadDto){
//        try{
//            Long loginUserId = jwtService.getUserIdx();
//            storyService.storyUpload(storyUploadDto,loginUserId);
//
//            return new BaseResponse<>("스토리 등록 요청 성공");
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//}
