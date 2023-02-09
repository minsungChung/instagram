package com.example.instagram_diana.src.service;


import com.example.instagram_diana.src.dto.PopularDto;
import com.example.instagram_diana.src.dto.likeStateDto;
import com.example.instagram_diana.src.repository.LikeRepository;
import com.example.instagram_diana.src.repository.likeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final likeDao likeStateDao;
    @Transactional
    public void Like(long postId,long loginUserId) {likeRepository.postLike(postId,loginUserId);}

    @Transactional
    public void unLike(long postId,long loginUserId) {likeRepository.postUnLike(postId,loginUserId);}


    @Transactional
    public long countPostLikes(long postId){
        return likeRepository.countBypostId(postId);
    }

    @Transactional
    public likeStateDto LikeState(long postId, long loginUserId){
        return likeStateDao.likeState(postId,loginUserId);
    }


    @Transactional
    public List<PopularDto> popularFeed() {
        return likeStateDao.popularFeed();
    }
}
