package com.example.instagram_diana.src.member.repository;

import com.example.instagram_diana.src.comment.model.CommentLike;
import com.example.instagram_diana.src.comment.model.PostComment;
import com.example.instagram_diana.src.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Long countByComment(PostComment postComment);

    Optional<CommentLike> findByCommentAndUser(PostComment comment, Member user);

    @Modifying(
            clearAutomatically = true
    )
    @Query("UPDATE CommentLike cl SET cl.status = :status where cl.id = :id")
    int updateStatus(@Param("status") String status, @Param("id") Long id);
}
