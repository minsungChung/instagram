package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value="SELECT * FROM Post WHERE userId IN (SELECT toUserId FROM Follow WHERE fromUserId=:loginUserId) ORDER BY postId DESC",nativeQuery=true)
    List<Post> userFollowingPosts(long loginUserId);

    Optional<Post> findById(Long postId);


    //Post findBypostId(long );
}
