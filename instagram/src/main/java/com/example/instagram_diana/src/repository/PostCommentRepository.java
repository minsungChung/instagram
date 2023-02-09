package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.model.Post;
import com.example.instagram_diana.src.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPost(Post post);

    List<PostComment> findByParentId(Long ParentId);

    Long countByParentId(Long parentId);

    @Modifying(
            clearAutomatically = true
    )
    @Query("UPDATE PostComment pc SET pc.status = :status where pc.id = :id")
    int updateStatus(@Param("status") String status, @Param("id") Long id);
}
