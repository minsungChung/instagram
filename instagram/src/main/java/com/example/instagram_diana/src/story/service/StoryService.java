package com.example.instagram_diana.src.story.service;

import com.example.instagram_diana.config.BaseException;
import com.example.instagram_diana.config.BaseResponseStatus;
import com.example.instagram_diana.src.comment.repository.PostCommentRepository;
import com.example.instagram_diana.src.member.model.Follow;
import com.example.instagram_diana.src.member.model.User;
import com.example.instagram_diana.src.member.repository.CommentLikeRepository;
import com.example.instagram_diana.src.member.repository.FollowRepository;
import com.example.instagram_diana.src.post.repository.PostRepository;
import com.example.instagram_diana.src.story.dto.*;
import com.example.instagram_diana.src.story.model.Story;
import com.example.instagram_diana.src.story.model.StoryCheck;
import com.example.instagram_diana.src.story.model.StroyMedia;
import com.example.instagram_diana.src.story.repository.StoryCheckRepository;
import com.example.instagram_diana.src.story.repository.StoryMediaRepository;
import com.example.instagram_diana.src.story.repository.StoryRepository;
import com.example.instagram_diana.src.member.repository.UserRepository;
import com.example.instagram_diana.src.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoryService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StoryRepository storyRepository;
    private final StoryMediaRepository storyMediaRepository;
    private final FollowRepository followRepository;
    private final StoryCheckRepository storyCheckRepository;
    private final PostCommentRepository postCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public StoryService(JwtService jwtService, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PostRepository postRepository, StoryRepository storyRepository, StoryMediaRepository storyMediaRepository, FollowRepository followRepository, StoryCheckRepository storyCheckRepository, PostCommentRepository postCommentRepository, CommentLikeRepository commentLikeRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postRepository = postRepository;
        this.storyRepository = storyRepository;
        this.storyMediaRepository = storyMediaRepository;
        this.followRepository = followRepository;
        this.storyCheckRepository = storyCheckRepository;
        this.postCommentRepository = postCommentRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    @Transactional
    public PostStoryRes uploadStory(Long userId, PostStoryReq postStoryReq) throws BaseException {
        try {
            Story story = Story.builder().user((User)this.userRepository.findById(userId).orElseThrow(() -> {
                return new IllegalArgumentException("user doesn't exist");
            })).contentType(postStoryReq.getContentType()).mediaUrl(postStoryReq.getMediaUrl()).postId(postStoryReq.getPostId()).build();
            Story resStory = (Story)this.storyRepository.save(story);
            Long storyId = resStory.getId();
            if (postStoryReq.getPostId() == null) {
                StroyMedia storyMedia = StroyMedia.builder().story((Story)this.storyRepository.findById(storyId).orElseThrow(() -> {
                    return new IllegalArgumentException("story doesn't exist");
                })).mediaType(postStoryReq.getContentType()).sourceUrl(postStoryReq.getMediaUrl()).build();
                this.storyMediaRepository.save(storyMedia);
            }

            return new PostStoryRes(userId, storyId);
        } catch (Exception var7) {
            this.logger.error("App - uploadStory Service Error", var7);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public PostStoryRes deleteStory(Long storyId) throws BaseException {
        try {
            Story story = (Story)this.storyRepository.findById(storyId).orElseThrow(() -> {
                return new IllegalArgumentException("story doesn't exist");
            });
            this.storyRepository.updateStatus("INACTIVE", storyId);
            return new PostStoryRes(story.getUser().getId(), storyId);
        } catch (Exception var3) {
            this.logger.error("App - Follow Service Error", var3);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public GetStoryRes watchStory(Long me, Long storyId) throws BaseException {
        try {
            Story story = (Story)this.storyRepository.findById(storyId).orElseThrow(() -> {
                return new IllegalArgumentException("story doesn't exist");
            });
            if (story.getStatus().equals("INACTIVE")) {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            } else {
                StoryCheck storyCheck = StoryCheck.builder().story((Story)this.storyRepository.findById(storyId).orElseThrow(() -> {
                    return new IllegalArgumentException("story doesn't exist");
                })).user((User)this.userRepository.findById(me).orElseThrow(() -> {
                    return new IllegalArgumentException("user doesn't exist");
                })).build();
                this.storyCheckRepository.save(storyCheck);
                return new GetStoryRes(story.getUser().getProfileUrl(), story.getUser().getUsername(), story.getMediaUrl(), story.getPostId(), story.getUpdatedAt());
            }
        } catch (Exception var5) {
            this.logger.error("App - WatchStory Service Error", var5);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public GetStoryViewersNumRes getStoryViewersNum(Long me, Long storyId) throws BaseException {
        try {
            Story story = (Story)this.storyRepository.findById(storyId).orElseThrow(() -> {
                return new IllegalArgumentException("story doesn't exist");
            });
            if (story.getStatus().equals("INACTIVE")) {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            } else if (story.getUser().getId() == me) {
                Long count = this.storyCheckRepository.countByStory(story);
                return new GetStoryViewersNumRes(count);
            } else {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception var5) {
            this.logger.error("App - WatchStory Service Error", var5);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetStoryViewersRes> getStoryViewers(Long me, Long storyId) throws BaseException {
        try {
            Story story = (Story)this.storyRepository.findById(storyId).orElseThrow(() -> {
                return new IllegalArgumentException("story doesn't exist");
            });
            if (story.getStatus().equals("INACTIVE")) {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            } else if (story.getUser().getId() == me) {
                List<StoryCheck> storyCheck = this.storyCheckRepository.findByStory(story);
                Stream<StoryCheck> stream = storyCheck.stream();
                List<GetStoryViewersRes> getStoryViewersRes = (List)stream.map((u) -> {
                    GetStoryViewersRes gvr = new GetStoryViewersRes(u.getUser().getProfileUrl(), u.getUser().getUsername());
                    return gvr;
                }).collect(Collectors.toList());
                return getStoryViewersRes;
            } else {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception var7) {
            this.logger.error("App - WatchStory Service Error", var7);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetStoriesRes> getStories(Long me, Long userId) throws BaseException {
        try {
            List<Follow> follow = this.followRepository.findByFromUserId(me);
            Stream<Follow> stream = follow.stream();
            List<GetStoriesRes> getStoriesRes = (List)stream.map((i) -> {
                System.out.println(i);
                if (this.storyRepository.countByUser((User)this.userRepository.findById(i.getToUser().getId()).get()) != 0L) {
                    User user = (User)this.userRepository.findById(i.getToUser().getId()).orElseThrow(() -> {
                        return new IllegalArgumentException("user doesn't exist");
                    });
                    GetStoriesRes gsr = new GetStoriesRes(user.getId(), user.getProfileUrl(), user.getUsername());
                    return gsr;
                } else {
                    return null;
                }
            }).collect(Collectors.toList());
            return getStoriesRes;
        } catch (Exception var6) {
            this.logger.error("App - WatchStory Service Error", var6);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Long> getStories(Long userId) throws BaseException {
        try {
            List<Story> story = this.storyRepository.findByUser((User)this.userRepository.findById(userId).orElse(null));
            Stream<Story> stream = story.stream();
            List<Long> getRes = (List)stream.map((s) -> {
                Long storyId = s.getId();
                return storyId;
            }).collect(Collectors.toList());
            return getRes;
        } catch (Exception var5) {
            this.logger.error("App - WatchStory Service Error", var5);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
