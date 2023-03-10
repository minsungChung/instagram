package com.example.instagram_diana.src.user;

import com.example.instagram_diana.config.BaseException;
import com.example.instagram_diana.src.dto.DayDto;
import com.example.instagram_diana.src.dto.userFollowingPostDto;
import com.example.instagram_diana.src.model.Post;
import com.example.instagram_diana.src.model.PostMedia;
import com.example.instagram_diana.src.repository.DayDao;
import com.example.instagram_diana.src.repository.FollowRepository;
import com.example.instagram_diana.src.model.User;
import com.example.instagram_diana.src.repository.PostMediaRepository;
import com.example.instagram_diana.src.repository.PostRepository;
import com.example.instagram_diana.src.service.LikeService;
import com.example.instagram_diana.src.service.PostMediaService;
import com.example.instagram_diana.src.testS3.S3Service;
import com.example.instagram_diana.src.user.model.*;
import com.example.instagram_diana.src.utils.JwtService;
import com.example.instagram_diana.src.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.instagram_diana.config.BaseResponseStatus.*;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    //private final PostService postService;

    private final PostRepository postRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostMediaRepository postMediaRepository;
    private final PostMediaService postMediaService;

    private final LikeService likeService;

    private final S3Service s3Service;

    private final DayDao dayDao;

    @Autowired
    public UserService(JwtService jwtService, UserRepository userRepository, FollowRepository followRepository, PostRepository postRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       PostMediaRepository postMediaRepository, PostMediaService postMediaService, LikeService likeService, S3Service s3Service, DayDao dayDao) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postRepository = postRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postMediaRepository = postMediaRepository;
        this.postMediaService = postMediaService;
        this.likeService = likeService;
        this.s3Service = s3Service;
        this.dayDao = dayDao;
    }

    public boolean checkUserExist(long userId){

        System.out.println("????????????????????? ????????? ???:"+ userRepository.existsById(userId));
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
    public User findUserById(long userIdx){
        User user = userRepository.findById(userIdx).get();
        return user;
    }

    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        String pwd;
        try{
            //????????? ()
            // ??????????????? bCrypt??? ?????????!
            pwd = new SHA256().encrypt(postUserReq.getPassword());//bCryptPasswordEncoder.encode(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            User user = User.builder()
                    .username(postUserReq.getUserName())
                    .name(postUserReq.getName())
                    .email(postUserReq.getEmail())
                    .phone(postUserReq.getPhone())
                    .password(postUserReq.getPassword())
                    .provider("INSTAGRAM")
                    .profileUrl("https://blog.kakaocdn.net/dn/c3vWTf/btqUuNfnDsf/VQMbJlQW4ywjeI8cUE91OK/img.jpg")
                    .build();

            // ???????????? (DB??????)

            User resUser = userRepository.save(user);
            Long userIdx = resUser.getId();

            //jwt ??????.
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

            // ?????????: ????????????/???????????????/????????? => ???????????? ?????? ??????
            String loginInput = postLoginReq.getLoginInput();

            User user = new User();
            try {
                // 1. ?????????????????? ???????????? DB?????? ?????? ????????????
                if (userRepository.existsByEmail(loginInput)) {
                    user = userRepository.findByEmail(loginInput);
                }
                // 2. ???????????? ????????? ?????????????????? DB?????? ?????? ????????????
                else if (userRepository.existsByUsername(loginInput)) {
                    user = userRepository.findByUsername(loginInput);
                }
                // 3. ??? ????????? ????????? DB?????? ?????? ????????????
                else if (userRepository.existsByPhone(loginInput)) {
                    user = userRepository.findByPhone(loginInput);
                }else{ // ???????????? ?????? ?????????
                   return null;
                }

                String encryptPwd;

                if (postLoginReq.getPassword() == "??????") { //Oauth2?????????????????????
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

        Optional<User> updateUser = userRepository.findById(userIdx);

        updateUser.ifPresent(user->{
            System.out.println("hi");
            if(patchUserReq.getName()!=null){user.setName(patchUserReq.getName());}
            if(patchUserReq.getUserName()!=null){user.setUsername(patchUserReq.getUserName());}
            if(patchUserReq.getBio()!=null){user.setBio(patchUserReq.getBio());}
            if(patchUserReq.getLink()!=null){user.setSite(patchUserReq.getLink());}
            if(patchUserReq.getProfileUrl()!=null){user.setProfileUrl(patchUserReq.getProfileUrl());}

            userRepository.save(user);

        });
    }

    @Transactional(readOnly = true)
    public UserProfileDto UserProfile(long pageUserId,long loginUserId) {

        UserProfileDto dto = new UserProfileDto();


        Optional<User> pageUser = userRepository.findById(pageUserId);
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
            dto.setPostCount(user.getPosts().size());

            int followState = followRepository.followState(loginUserId,pageUserId);
            int followingCount = followRepository.followingCount(pageUserId);
            int followerCount = followRepository.followerCount(pageUserId);

            dto.setFollowState(followState == 1);
            dto.setFollowing(followingCount);
            dto.setFollower(followerCount);

            // ?????? ????????????
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

        // ?????? ????????? ????????????
        List<Post> userFollowingPosts = postRepository.userFollowingPosts(loginUserId);

        // ?????? ????????? ??????????????? info dto list
        List<userFollowingPostDto> dtos = new ArrayList<>(userFollowingPosts.size());

        // dtos??? ??? post?????? ????????? ??????
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

            // ????????? ?????????
            dto.setPostUserId(post.getUser().getId());

            // loginUser ??? ??????????????? ???????????? ????????????
            int likeState = likeService.LikeState(post.getId(),loginUserId).getLikeState();
            dto.setLikeState(likeState);

            // dayDao ???????????????
            DayDto dayInfo = dayDao.monthOfDay(post.getId());
            dto.setDayInfo(dayInfo);

            dtos.add(dto);

        });
        return dtos;
    }

    @Transactional
    public String profileUpload(long loginUserId, String profileImgUrl) throws IOException {
        User user = findUserById(loginUserId);

//        ProfileImageUploadDto dto = new ProfileImageUploadDto();
//
//        // s3??? ?????? ??????
//        String profileImgUrl = s3Service.uploadFile(imgFile);
//        dto.setProfileImageUrl(profileImgUrl);
//
//        // ??????????????? ????????????
//        user.setProfileUrl(profileImgUrl);

        user.setProfileUrl(profileImgUrl);

        // table??? ??????
        userRepository.save(user);

        return user.getProfileUrl();
    }

    @Transactional
    public void modifyUserEmail(long userId, String email) {
        User user = userRepository.findById(userId).get();

        user.setEmail(email);
        userRepository.save(user);
    }

    public void modifyUserPhone(long userId, String phone) {
        User user = userRepository.findById(userId).get();

        user.setPhone(phone);
        userRepository.save(user);
    }
}
