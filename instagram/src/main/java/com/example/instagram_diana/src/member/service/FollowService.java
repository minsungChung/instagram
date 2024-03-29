package com.example.instagram_diana.src.member.service;
import com.example.instagram_diana.src.member.dto.FollowUserDto;
import com.example.instagram_diana.src.member.repository.FollowDao;
import com.example.instagram_diana.src.member.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final FollowDao followDao; // em구현체; repo




    @Transactional(readOnly = true)
    public List<FollowUserDto> followingList(long loginUserId, long pageUserId){
        return followDao.followingList(loginUserId,pageUserId);
    }

    @Transactional
    public void follow(long fromUserId,long toUserId){

        followRepository.mFollow(fromUserId,toUserId);
    }


    @Transactional
    public void unFollow(Long fromUserId,Long toUserId){

        followRepository.mUnFollow(fromUserId,toUserId);
    }

    @Transactional
    public int checkFollow(Long fromUserId, Long toUserId) {

        return followRepository.checkFollow(fromUserId,toUserId);
    }

    public List<FollowUserDto> followerList(long loginUserId, long pageUserId) {
        return followDao.followerList(loginUserId,pageUserId);
    }
}
