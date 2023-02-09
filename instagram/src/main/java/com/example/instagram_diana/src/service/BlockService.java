package com.example.instagram_diana.src.service;

import com.example.instagram_diana.src.repository.BlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockService {
    private final BlockRepository blockRepository;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @Transactional
    public int checkBlock(Long fromUserId, Long toUserId) {

        return blockRepository.checkBlock(fromUserId,toUserId);
    }

    public void block(Long fromUserId, long toUserId) {
        blockRepository.block(fromUserId,toUserId);
    }

    @Transactional
    public void unblock(Long fromUserId, long toUserId) {
        blockRepository.unBlock(fromUserId,toUserId);
    }
}
