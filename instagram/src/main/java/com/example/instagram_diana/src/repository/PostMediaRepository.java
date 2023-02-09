package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.model.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia,Long> {

    @Modifying
    @Query(value="SELECT * FROM postMedia WHERE postMediaId= any(SELECT MIN(postMediaId) FROM  Post join postMedia on Post.postId=postMedia.postId where userId=:PageUserId group by Post.postId);",nativeQuery = true)
    List<PostMedia> userPageThumbnails(long PageUserId);

//    @Modifying
//    @Query(value="SELECT * FROM postMedia WHERE postId=2;",nativeQuery = true)
//    List<PostMedia> mediaUrls(long postId);

    List<PostMedia> findAllBypostId(long postId);

    @Query(value = "SELECT COUNT(*) FROM postMedia WHERE postId=:postId",nativeQuery = true)
    int countMedia(long postId);

    PostMedia findBypostId(long postId);
}
