package com.example.instagram_diana.src.member.service;

import com.example.instagram_diana.src.common.exception.BaseException;
import com.example.instagram_diana.src.member.dto.*;
import com.example.instagram_diana.src.member.model.Member;
import com.example.instagram_diana.src.member.repository.UserRepository;
import com.example.instagram_diana.src.post.dto.DayDto;
import com.example.instagram_diana.src.post.model.Post;
import com.example.instagram_diana.src.post.model.PostMedia;
import com.example.instagram_diana.src.post.repository.DayDao;
import com.example.instagram_diana.src.member.repository.FollowRepository;
import com.example.instagram_diana.src.post.repository.PostMediaRepository;
import com.example.instagram_diana.src.post.repository.PostRepository;
import com.example.instagram_diana.src.post.service.PostMediaService;
import com.example.instagram_diana.src.utils.JwtService;
import com.example.instagram_diana.src.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.instagram_diana.src.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final PostMediaService postMediaService;

    private final LikeService likeService;

    private final DayDao dayDao;

    public boolean checkUserExist(long userId){

        System.out.println("체크유저아이디 서비스 값:"+ userRepository.existsById(userId));
        return userRepository.existsById(userId);
    }
    public boolean checkEmailDuplicate(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean checkPhoneDuplicate(String phone){
        return userRepository.existsByPhone(phone);
    }

    public boolean checkNameDuplicate(String userName) {
        return userRepository.existsByUsername(userName);
    }

    @Transactional(readOnly = true)
    public Member findUserById(long userIdx){
        Member user = userRepository.findById(userIdx).get();
        return user;
    }

    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        String pwd;
        try{
            //암호화 ()
            // 통일성위해 bCrypt로 로그인!
            pwd = new SHA256().encrypt(postUserReq.getPassword());//bCryptPasswordEncoder.encode(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            Member user = Member.builder()
                    .username(postUserReq.getUserName())
                    .name(postUserReq.getName())
                    .email(postUserReq.getEmail())
                    .phone(postUserReq.getPhone())
                    .password(postUserReq.getPassword())
                    .provider("INSTAGRAM")
                    .profileUrl("https://blog.kakaocdn.net/dn/c3vWTf/btqUuNfnDsf/VQMbJlQW4ywjeI8cUE91OK/img.jpg")
                    .build();

            // 회원가입 (DB저장)

            Member resUser = userRepository.save(user);
            Long userIdx = resUser.getId();

            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            logger.error("App - createUser Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PostUserRes logIn(PostLoginReq postLoginReq) throws BaseException {
        try {

            // 아이디: 전화번호/사용자이름/이메일 => 비밀번호 찾기 로직
            String loginInput = postLoginReq.getLoginInput();

            Member user = new Member();
            try {
                // 1. 이메일존재시 이메일로 DB에서 유저 찾아오기
                if (userRepository.existsByEmail(loginInput)) {
                    user = userRepository.findByEmail(loginInput);
                }
                // 2. 유저네임 존재시 유저네임으로 DB에서 유저 찾아오기
                else if (userRepository.existsByUsername(loginInput)) {
                    user = userRepository.findByUsername(loginInput);
                }
                // 3. 폰 존재시 폰으로 DB에서 유저 찾아오기
                else if (userRepository.existsByPhone(loginInput)) {
                    user = userRepository.findByPhone(loginInput);
                }else{ // 존재하지 않는 값일때
                   return null;
                }

                String encryptPwd;

                if (postLoginReq.getPassword() == "없음") { //Oauth2로그인유저처리
                    Long userIdx = user.getId();
                    String jwt = jwtService.createJwt(userIdx);
                    return new PostUserRes(jwt, userIdx);
                } else {

                    try {
                        encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());

                    } catch (Exception exception) {
                        logger.error("App - logIn Provider Encrypt Error", exception);
                        throw new BaseException(PASSWORD_DECRYPTION_ERROR);
                    }

                    if (user.getPassword().equals(encryptPwd)) {
                        Long userIdx = user.getId();
                        String jwt = jwtService.createJwt(userIdx);
                        return new PostUserRes(jwt, userIdx);
                    } else {
                        throw new BaseException(FAILED_TO_LOGIN);
                    }
                }

            } catch (Exception exception) {
                throw new BaseException(FAILED_TO_LOGIN);
            }
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }

        }

    @Transactional
    public void modifyUserInfo(Long userIdx, PatchUserReq patchUserReq) throws BaseException {

        Optional<Member> updateUser = userRepository.findById(userIdx);

        updateUser.ifPresent(user->{
            user.updateName(patchUserReq.getName());
            user.updateUsername(patchUserReq.getProfileUrl());
            user.updateBio(patchUserReq.getBio());
            user.updateSite(patchUserReq.getLink());
            user.updateProfileUrl(patchUserReq.getProfileUrl());
        });
    }

    @Transactional(readOnly = true)
    public UserProfileDto UserProfile(long pageUserId,long loginUserId) {

        UserProfileDto dto = new UserProfileDto();


        Optional<Member> pageUser = userRepository.findById(pageUserId);
        pageUser.ifPresent(user->{

            UserProfileUserDto PageUserInfo = UserProfileUserDto
                    .builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .bio(user.getBio())
                    .profileUrl(user.getProfileUrl())
                    .site(user.getSite())
                    .build();

            dto.setProfileUserDto(PageUserInfo);
            dto.setPageOwnerState(pageUserId==loginUserId);
            dto.setPostCount(1);

            int followState = followRepository.followState(loginUserId,pageUserId);
            int followingCount = followRepository.followingCount(pageUserId);
            int followerCount = followRepository.followerCount(pageUserId);

            dto.setFollowState(followState == 1);
            dto.setFollowing(followingCount);
            dto.setFollower(followerCount);

            // 대표 이미지들
            List<PostMedia> postMedias = postMediaRepository.userPageThumbnails(pageUserId);
            //ThumbnailsUrls
            //List<String> ThumbnailsUrls = null;
            System.out.println(postMedias);
            List<String> ThumbnailsUrls = new ArrayList<>(postMedias.size());

            postMedias.forEach((postMedia)->{
                ThumbnailsUrls.add(postMedia.getMediaUrl());
            });

            dto.setThumbnailUrls(ThumbnailsUrls);
        });
        return dto;
    }

    public List<userFollowingPostDto> userFollowingPosts(long loginUserId) {

        // 유저 팔로잉 게시물들
        List<Post> userFollowingPosts = postRepository.userFollowingPosts(loginUserId);

        // 유저 팔로잉 게시물들의 info dto list
        List<userFollowingPostDto> dtos = new ArrayList<>(userFollowingPosts.size());

        // dtos에 각 post정보 하나씩 추가
        userFollowingPosts.forEach((post)->{
            userFollowingPostDto dto = new userFollowingPostDto();
            dto.setPostId(post.getId());
            dto.setUserName(post.getUser().getUsername());
            dto.setContent(post.getContent());

            List<String> postMediaUrls = postMediaService.getMediaUrls(post.getId());

            long count = likeService.countPostLikes(post.getId());
            dto.setImgUrls(postMediaUrls);
            dto.setLikeCount(count);
            dto.setCommentCount(5);

            // 추가된 부분들
            dto.setPostUserId(post.getUser().getId());

            // loginUser 가 해당페이지 좋아요한 상태인지
            int likeState = likeService.LikeState(post.getId(),loginUserId).getLikeState();
            dto.setLikeState(likeState);

            // dayDao 가져와담기
            DayDto dayInfo = dayDao.monthOfDay(post.getId());
            dto.setDayInfo(dayInfo);

            dtos.add(dto);

        });
        return dtos;
    }

    @Transactional
    public String profileUpload(long loginUserId, String profileImgUrl) throws IOException {
        Member user = findUserById(loginUserId);

//        ProfileImageUploadDto dto = new ProfileImageUploadDto();
//
//        // s3에 파일 저장
//        String profileImgUrl = s3Service.uploadFile(imgFile);
//        dto.setProfileImageUrl(profileImgUrl);
//
//        // 프로필정보 업데이트
//        user.setProfileUrl(profileImgUrl);

        user.updateProfileUrl(profileImgUrl);

        // table에 저장
        userRepository.save(user);

        return user.getProfileUrl();
    }

    @Transactional
    public void modifyUserEmail(long userId, String email) {
        Member user = userRepository.findById(userId).get();

        user.updateEmail(email);
    }

    public void modifyUserPhone(long userId, String phone) {
        Member user = userRepository.findById(userId).get();

        user.updatePhone(phone);
    }
}
