package com.example.instagram_diana.src.member.repository;

import com.example.instagram_diana.src.member.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BlockRepository extends JpaRepository<Block,Long> {

    @Transactional
    @Query(value = "SELECT EXISTS(select * from Block where fromUserId=:fromUserId and toUserId = :toUserId)",nativeQuery = true)
    int checkBlock(Long fromUserId, Long toUserId);

    @Transactional
    @Modifying
    @Query(value="INSERT INTO Block(fromUserId,toUserId,createdAt,status,updatedAt) VALUES(:fromUserId,:toUserId,now(),'ACTIVE',now())",nativeQuery = true)
    void block(Long fromUserId, long toUserId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Block WHERE fromUserId = :fromUserId AND toUserId =:toUserId",nativeQuery = true)
    void unBlock(Long fromUserId, long toUserId);
}
