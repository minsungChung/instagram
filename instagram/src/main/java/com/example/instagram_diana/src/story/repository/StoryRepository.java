package com.example.instagram_diana.src.story.repository;

import com.example.instagram_diana.src.member.model.Member;
import com.example.instagram_diana.src.story.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    @Modifying(
            clearAutomatically = true
    )
    @Query("UPDATE Story s SET s.status = :status where s.id = :id")
    int updateStatus(@Param("status") String status, @Param("id") Long id);

    Long countByUser(Member user);

    List<Story> findByUser(Member user);

}

