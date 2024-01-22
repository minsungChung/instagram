package com.example.instagram_diana.src.post.service;

import com.example.instagram_diana.src.post.model.PostMedia;
import com.example.instagram_diana.src.post.repository.PostMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostMediaService {
    private final PostMediaRepository postMediaRepository;

    // post id에 해당하는 img urls list 리턴
    public List<String> getMediaUrls(long postId){
        List<String> imgUrls = new ArrayList<>();
        System.out.println("포스트아이디값전달받나테스트!!!!!!!!!!!!!!!!!!!!!"+postId);


            //List<PostMedia> postMedias= postMediaRepository.mediaUrls(postId);
            List<PostMedia> postMedias= postMediaRepository.findAllBypostId(postId);
            System.out.println("포스트값을전달받았다!!!!!!!!!!!!?????????????????????33333333333");

            postMedias.forEach(postMedia -> {
                imgUrls.add(postMedia.getMediaUrl());
            });

            return imgUrls;
        }
    }

