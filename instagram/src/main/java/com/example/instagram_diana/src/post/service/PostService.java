package com.example.instagram_diana.src.post.service;


import com.example.instagram_diana.src.common.exception.BaseException;
import com.example.instagram_diana.src.member.model.Member;
import com.example.instagram_diana.src.post.model.Post;
import com.example.instagram_diana.src.post.model.PostMedia;
import com.example.instagram_diana.src.post.dto.*;
import com.example.instagram_diana.src.post.repository.DayDao;
import com.example.instagram_diana.src.post.repository.PostMediaRepository;
import com.example.instagram_diana.src.post.repository.PostRepository;
import com.example.instagram_diana.src.member.service.LikeService;
import com.example.instagram_diana.src.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.instagram_diana.src.common.response.BaseResponseStatus.POST_CANNOT_DELETE;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final UserService userService;

    private final PostMediaService postMediaService;
    private final LikeService likeService;

    private final DayDao dayDao;

    @Transactional
    public void postUpload(PostUploadDto postUploadDto, Long loginId) {

        // post DB저장
        Member user = userService.findUserById(loginId);

        Post post = Post.builder()
                .user(user)
                .content(postUploadDto.getContent())
                .build();

        postRepository.save(post);

        System.out.println(postUploadDto.getImgUrlList()+" 게시물 업로드 요청입니다.");

        // postMedia 모두 별도 DB저장
        for(int i=0;i<postUploadDto.getImgUrlList().size();i++){
            PostMedia postMedia = PostMedia.builder()
                    .post(post)
                    .mediaType("PHOTO")
                    .mediaUrl(postUploadDto.getImgUrlList().get(i)).build();

            postMediaRepository.save(postMedia);
        }
    }

    @Transactional
    public boolean checkPostExist(long postId){
        return postRepository.existsById(postId);
    }


    @Transactional
    public long getPostWriter(long postId){
        Post postEntity= postRepository.findById(postId).get();
        return postEntity.getUser().getId();
    }

    @Transactional
    public List<Post> getUserFollowingPosts(long loignUserId){
        return postRepository.userFollowingPosts(loignUserId);
    }


    @Transactional
    //포스트 내용 수정
    public void modifyPostContent(long postId, Long loginUserId,String content) {

        Post post = postRepository.findById(postId).get();

        post.uploadContent(content);
    }

    @Transactional
    public DayDto updateDay(long postId) {
        // dayDao 가져와담기
        DayDto dayInfo = dayDao.monthOfDay(postId);
        return dayInfo;

    }

    @Transactional
    public void deletePost(long postId,long loginUserId) throws BaseException {

        Post post = postRepository.findById(postId).get();

        // 유저 아이디가 다를시 에러
        if(post.getUser().getId()!=loginUserId) {
            throw  new BaseException(POST_CANNOT_DELETE);

        }

        postRepository.deleteById(postId);
    }

    @Transactional
    public void feedUpload(long loginUserId, FeedUploadDto feedUploadDto) {
        // post DB저장
        Member user = userService.findUserById(loginUserId);

        Post post = Post.builder()
                .user(user)
                .content(feedUploadDto.getContent()).build();

        postRepository.save(post);

        // postMedia 모두 별도 DB저장
        PostMedia postMedia = PostMedia.builder()
                .post(post)
                .mediaType("PHOTO")
                .mediaUrl(feedUploadDto.getImgUrl()).build();

        postMediaRepository.save(postMedia);
    }

    @Transactional
    public DayDetailDto postDayDetail(Long postId) {
        return dayDao.postDayDetail(postId);
    }

    // 돋보기 랜덤피드 디테일
    @Transactional
    public PopularDetailDto popularFeedDtail(long postId, long loginUserId) {

        PopularDetailDto dto = new PopularDetailDto();

        Post post = postRepository.findById(postId).get();
        dto.setPostId(post.getId());
        dto.setUserName(post.getUser().getUsername());
        dto.setContent(post.getContent());


        List<String> postMediaUrls = postMediaService.getMediaUrls(post.getId());
        long count = likeService.countPostLikes(post.getId());
        dto.setImgUrls(postMediaUrls);
        dto.setLikeCount(count);
        dto.setPostUserId(post.getUser().getId());

        // loginUser 가 해당페이지 좋아요한 상태인지
        int likeState = likeService.LikeState(post.getId(),loginUserId).getLikeState();
        dto.setLikeState(likeState);

        // dayInfo
        DayDetailDto dayDetailDto = dayDao.postDayDetail(postId);
        dto.setDayDetailDto(dayDetailDto);

        return dto;



    }
}
