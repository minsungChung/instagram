package com.example.instagram_diana.src.post.controller;

import com.example.instagram_diana.src.common.exception.BaseException;
import com.example.instagram_diana.src.common.response.BaseResponse;
import com.example.instagram_diana.src.post.dto.*;
import com.example.instagram_diana.src.member.service.LikeService;
import com.example.instagram_diana.src.post.service.PostService;
import com.example.instagram_diana.src.testS3.S3Service;
import com.example.instagram_diana.src.member.service.UserService;
import com.example.instagram_diana.src.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.example.instagram_diana.src.common.response.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class PostController {

    private final UserService userService;
    private final PostService postService;
    private final S3Service s3Service;
    private final JwtService jwtService;
    private final LikeService likeService;

    // 랜덤 피드 개별 게시물 조회
    @GetMapping("/posts/popular/{postId}")
    public BaseResponse<?> popularFeedDetail(@PathVariable("postId") Long postId){
        try {

            // 주소의 포스트번호가 없으면 에러
            if (!postService.checkPostExist(postId)){
                return new BaseResponse<>(POST_ID_NOT_EXISTS);
            }

            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();
            PopularDetailDto dtos = postService.popularFeedDtail(postId,loginUserId);
            return new BaseResponse<>("포스트 정보 요청 성공",dtos);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 좋아요 순 피드
    @GetMapping("/posts/popular")
    public BaseResponse<?> popularFeed(){

        List<PopularDto> popularDtoList = likeService.popularFeed();


        return new BaseResponse<>(popularDtoList);

    }

    // 포스트 단일 업로드
    @PostMapping("/post")
    public BaseResponse<?> feedUpload(@RequestBody FeedUploadDto feedUploadDto){

        if(feedUploadDto.getImgUrl()==null){
            return new BaseResponse(FILE_CANNOT_NULL);
        }

        try{
            // jwt에서 idx 추출.
            long loginUserId = jwtService.getUserIdx();

            postService.feedUpload(loginUserId,feedUploadDto);

            return new BaseResponse<>("피드 업로드 요청 성공");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    // 날짜 정보
    @GetMapping("/posts/{postId}/day-details")
    public BaseResponse<?> postDayDetail(@PathVariable("postId") Long postId){
        // 주소의 포스트번호가 없으면 에러
        if (!postService.checkPostExist(postId)){
            return new BaseResponse<>(POST_ID_NOT_EXISTS);
        }

        DayDetailDto dayDetail= postService.postDayDetail(postId);
        return new BaseResponse<>(dayDetail);


    }

    @PostMapping("/posts")
    public BaseResponse<?> postUpload(@RequestBody PostUploadDto postUploadDto){
        try{
            Long loginUserId = jwtService.getUserIdx();
            postService.postUpload(postUploadDto,loginUserId);

            return new BaseResponse<>("게시물 등록 요청 성공");
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //@PathVariable("userId") long PageUserId X. form을 주소단에서 받을 수 없음.
    //MultipartFile post-upload.
    @PostMapping("/posts/multifiles")
    public BaseResponse<?> multiFilePostUpload(@RequestParam("userId") Long PageUserId,
                                      @RequestParam("content") String content,
                                      @RequestParam("multiFile") List<MultipartFile> multipartFiles) throws IOException {
        System.out.println("멀티파일 업로드 테스트컨트롤러입니다.");
        System.out.println("인풋값 받나 테스트: "+content+" and "+PageUserId);

        try{
            // 유저번호가 없으면 에러
            if (!userService.checkUserExist(PageUserId)){
                return new BaseResponse<>(USER_ID_NOT_EXIST);
            }

            // posts를 올릴 수 있는지 권한 확인(userId와 jwtId일치해야함)

            //--인증부분!!!!!!!!!! --------------------------------
//            Long LoginId = jwtService.getUserIdx();
//
//            if(PageUserId!=LoginId){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }

            Long LoginId=PageUserId;

            //--인증부분!!!!!!!!!! --------------------------------

            if (multipartFiles == null) {   // 첨부파일 없을 시 에러.
                throw new BaseException(POST_FILE_EMPTY);
            }else{
                // s3 서버에 이미지 저장
                List<String> imgUrlList = s3Service.upload(multipartFiles);
                System.out.println("Controller-imgUrlList: "+imgUrlList);

                // 서비스에 전달할 게시물 dto생성
                PostUploadDto postUploadDto = new PostUploadDto();
                postUploadDto.setUserId(LoginId);
                postUploadDto.setContent(content);
                postUploadDto.setImgUrlList(imgUrlList);

                postService.postUpload(postUploadDto,LoginId);
            }

            return new BaseResponse<>("포스트 업로드요청에 성공했습니다.");

    }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/likes/{postId}")
    public BaseResponse<?> likes(@PathVariable long postId){

        try{

            // 주소의 포스트번호가 없으면 에러
            if (!postService.checkPostExist(postId)){
                return new BaseResponse<>(POST_ID_NOT_EXISTS);
            }

            Long loginUserId = jwtService.getUserIdx();

            // 게시물 작성자와 좋아요 유저가 같으면 에러
            if (postService.getPostWriter(postId)==loginUserId) {
                return new BaseResponse<>(POST_CANNOT_MYSELF);

            }

            likeService.Like(postId,loginUserId);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
        return new BaseResponse<>("좋아요 요청에 성공했습니다.");
    }

    @PostMapping("/unlikes/{postId}")
    public BaseResponse<?> unlikes(@PathVariable long postId){

        try{
            // 주소의 유저번호가 없으면 에러
            if (!postService.checkPostExist(postId)){
                return new BaseResponse<>(POST_ID_NOT_EXISTS);
            }

            Long loginUserId = jwtService.getUserIdx();

            likeService.unLike(postId,loginUserId);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
        return new BaseResponse<>("좋아요 취소 요청에 성공했습니다.");
    }

    // 포스트 수정(hashmap으로 json받기)
    @PatchMapping("/posts/{postId}")
    public BaseResponse<?> modifyPostContent(@PathVariable("postId") long postId, @RequestBody HashMap<Object,String> hash){
        // 주소의 포스트번호가 없으면 에러
        if (!postService.checkPostExist(postId)){
            return new BaseResponse<>(POST_ID_NOT_EXISTS);
        }

        try{
            System.out.println(hash.get("content")+"!!!!!!2222222222222222");
            Long loginUserId = jwtService.getUserIdx();
            postService.modifyPostContent(postId,loginUserId,hash.get("content"));
            return new BaseResponse<>("게시물 내용 수정에 성공했습니다.");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @GetMapping("/posts/{postId}/days")
    public BaseResponse<DayDto> updateDay(@PathVariable("postId") long postId){
        // 주소의 포스트번호가 없으면 에러
        if (!postService.checkPostExist(postId)){
            return new BaseResponse<>(POST_ID_NOT_EXISTS);
        }

        DayDto day = postService.updateDay(postId);
        return new BaseResponse<>(day);


    }

    // 포스트 삭제
    @DeleteMapping ("/feeds/{postId}")
    public BaseResponse<?> deletePost(@PathVariable("postId") long postId){

        // 주소의 포스트번호가 없으면 에러
        if (!postService.checkPostExist(postId)){
            return new BaseResponse<>(POST_ID_NOT_EXISTS);
        }

        try{
            Long loginUserId = jwtService.getUserIdx();

            postService.deletePost(postId,loginUserId);
            return new BaseResponse<>("게시물 삭제 요청이 성공했습니다.");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }


}

