package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.model.Story;
import com.example.instagram_diana.src.model.StoryCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryCheckRepository extends JpaRepository<StoryCheck, Long> {
    Long countByStory(Story story);

    List<StoryCheck> findByStory(Story story);
}
