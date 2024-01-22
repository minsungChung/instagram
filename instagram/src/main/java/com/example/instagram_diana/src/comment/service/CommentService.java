package com.example.instagram_diana.src.comment.service;

import com.example.instagram_diana.config.BaseException;
import com.example.instagram_diana.config.BaseResponseStatus;
import com.example.instagram_diana.src.comment.dto.GetCommentRes;
import com.example.instagram_diana.src.comment.repository.PostCommentRepository;
import com.example.instagram_diana.src.comment.dto.PostCommentLikeRes;
import com.example.instagram_diana.src.comment.dto.PostCommentReq;
import com.example.instagram_diana.src.comment.dto.PostCommentRes;
import com.example.instagram_diana.src.member.repository.CommentLikeRepository;
import com.example.instagram_diana.src.member.repository.FollowRepository;
import com.example.instagram_diana.src.comment.model.CommentLike;
import com.example.instagram_diana.src.post.model.Post;
import com.example.instagram_diana.src.comment.model.PostComment;
import com.example.instagram_diana.src.member.model.User;
import com.example.instagram_diana.src.post.repository.PostRepository;
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
public class CommentService {
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
    public CommentService(JwtService jwtService, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PostRepository postRepository, StoryRepository storyRepository, StoryMediaRepository storyMediaRepository, FollowRepository followRepository, StoryCheckRepository storyCheckRepository, PostCommentRepository postCommentRepository, CommentLikeRepository commentLikeRepository) {
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
    public PostCommentRes uploadComment(Long userId, Long postId, PostCommentReq postCommentReq) throws BaseException {
        try {
            PostComment postComment = PostComment.builder().post((Post)this.postRepository.findById(postId).orElseThrow(() -> {
                return new IllegalArgumentException("post doesn't exist");
            })).user((User)this.userRepository.findById(userId).orElseThrow(() -> {
                return new IllegalArgumentException("user doesn't exist");
            })).content(postCommentReq.getContent()).parentId(0L).build();
            PostComment resPostComment = (PostComment)this.postCommentRepository.save(postComment);
            Long commentId = resPostComment.getId();
            return new PostCommentRes(userId, commentId, resPostComment.getContent());
        } catch (Exception var7) {
            this.logger.error("App - uploadStory Service Error", var7);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public PostCommentRes deleteComment(Long postId) throws BaseException {
        try {
            PostComment postComment = (PostComment)this.postCommentRepository.findById(postId).orElseThrow(() -> {
                return new IllegalArgumentException("comment doesn't exist");
            });
            this.postCommentRepository.updateStatus("INACTIVE", postId);
            return new PostCommentRes(postComment.getUser().getId(), postId, postComment.getContent());
        } catch (Exception var3) {
            this.logger.error("App - Follow Service Error", var3);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetCommentRes> getComments(Long postId) throws BaseException {
        try {
            Post post = (Post)this.postRepository.findById(postId).orElseThrow(() -> {
                return new IllegalArgumentException("post doesn't exist");
            });
            List<PostComment> postComments = this.postCommentRepository.findByPost(post);
            Stream<PostComment> stream = postComments.stream();
            List<GetCommentRes> getCommentRes = (List)stream.map((c) -> {
                User user = c.getUser();
                Long likeNum = this.commentLikeRepository.countByComment(c);
                Long reComNum = this.postCommentRepository.countByParentId(c.getId());
                GetCommentRes gcr = new GetCommentRes(user.getProfileUrl(), user.getUsername(), c.getContent(), reComNum, likeNum, c.getUpdatedAt());
                return gcr;
            }).collect(Collectors.toList());
            return getCommentRes;
        } catch (Exception var6) {
            this.logger.error("App - Comment Service Error", var6);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public PostCommentRes uploadReComment(Long userId, Long postId, Long parentId, PostCommentReq postCommentReq) throws BaseException {
        try {
            PostComment postComment = PostComment.builder().post((Post)this.postRepository.findById(postId).orElseThrow(() -> {
                return new IllegalArgumentException("post doesn't exist");
            })).user((User)this.userRepository.findById(userId).orElseThrow(() -> {
                return new IllegalArgumentException("user doesn't exist");
            })).content(postCommentReq.getContent()).parentId(parentId).build();
            PostComment resPostComment = (PostComment)this.postCommentRepository.save(postComment);
            Long commentId = resPostComment.getId();
            return new PostCommentRes(userId, commentId, resPostComment.getContent());
        } catch (Exception var8) {
            this.logger.error("App - uploadStory Service Error", var8);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public List<GetCommentRes> getReComments(Long postId, Long commentId) throws BaseException {
        try {
            Post post = (Post)this.postRepository.findById(postId).orElseThrow(() -> {
                return new IllegalArgumentException("post doesn't exist");
            });
            List<PostComment> postComments = this.postCommentRepository.findByParentId(commentId);
            Stream<PostComment> stream = postComments.stream();
            List<GetCommentRes> getCommentRes = (List)stream.map((c) -> {
                User user = c.getUser();
                Long likeNum = this.commentLikeRepository.countByComment(c);
                Long reComNum = this.postCommentRepository.countByParentId(c.getId());
                GetCommentRes gcr = new GetCommentRes(user.getProfileUrl(), user.getUsername(), c.getContent(), reComNum, likeNum, c.getUpdatedAt());
                return gcr;
            }).collect(Collectors.toList());
            return getCommentRes;
        } catch (Exception var7) {
            this.logger.error("App - Comment Service Error", var7);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public PostCommentLikeRes uploadCommentLike(Long me, Long commentId) throws BaseException {
        try {
            String stat = "ACTIVE";
            PostComment postComment = (PostComment)this.postCommentRepository.findById(commentId).orElseThrow(() -> {
                return new IllegalArgumentException("comment doesn't exist");
            });
            User user = (User)this.userRepository.findById(me).orElseThrow(() -> {
                return new IllegalArgumentException("user doesn't exist");
            });
            CommentLike commentLike = (CommentLike)this.commentLikeRepository.findByCommentAndUser(postComment, user).get();
            if (commentLike != null) {
                if (commentLike.getStatus().equals("ACTIVE")) {
                    stat = "INACTIVE";
                }

                this.commentLikeRepository.updateStatus(stat, commentLike.getId());
                return new PostCommentLikeRes(commentLike.getId(), me);
            } else {
                CommentLike postCommentLike = CommentLike.builder().comment(postComment).user(user).status(stat).build();
                CommentLike resCommentLike = (CommentLike)this.commentLikeRepository.save(postCommentLike);
                Long commentLikeId = resCommentLike.getId();
                return new PostCommentLikeRes(commentLikeId, me);
            }
        } catch (Exception var10) {
            this.logger.error("App - Comment Service Error", var10);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
