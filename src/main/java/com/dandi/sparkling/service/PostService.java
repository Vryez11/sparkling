package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.CreatePostRequest;
import com.dandi.sparkling.dto.CreatePostResponse;
import com.dandi.sparkling.entity.Post;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.repository.PostRepository;
import com.dandi.sparkling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreatePostResponse createPost(Long userId, CreatePostRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        return CreatePostResponse.from(postRepository.save(post).getId());
    }
}
