package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<PostLike,Long> {

    @Modifying
    @Query(value="INSERT INTO postLike(postId,userId,updatedAt,createdAt,status) VALUES(:postId,:loginUserId,now(),now(),'ACTIVE')",nativeQuery = true)
    int postLike(long postId,long loginUserId);

    @Modifying
    @Query(value = "DELETE FROM postLike WHERE postId = :postId AND userId= :loginUserId",nativeQuery = true)
    int postUnLike(long postId,long loginUserId);

    Long countBypostId(long postId);

//    @Modifying
//    @Query(value = "SELECT exists( SELECT * FROM postLike WHERE EXISTS (SELECT * FROM postLike where (postId=:postId and userId=:loginUserId))) AS likeState;",nativeQuery = true)
//    int likeState(long postId,long loginUserId);

}
