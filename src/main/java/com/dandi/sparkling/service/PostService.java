package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.*;
import com.dandi.sparkling.entity.Post;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.repository.PostRepository;
import com.dandi.sparkling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public List<PostResponse> getPostList() {

        List<Post> all = postRepository.findAll();

        all.sort((p1, p2) -> {
            return p1.getCreatedAt().compareTo(p2.getCreatedAt());
        });

        List<String> nickNameList = all.stream()
                .map(p -> p.getUser().getNickname())
                .toList();

        List<PostResponse> posts = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {

            posts.add(PostResponse.from(all.get(i).getId(), all.get(i).getTitle(), nickNameList.get(i), all.get(i).getCreatedAt()));
        }

        return posts;
    }

    @Transactional
    public PostDetailResponse postDetail(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 Post를 찾을 수 없습니다."));

        return PostDetailResponse.from(post);
    }

    @Transactional
    public PostDetailResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 Post를 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 게시글에 권한이 없습니다.");
        }

        String title = request.getTitle();
        if (title != null && !title.trim().isBlank()) {
            post.updateTitle(title);
        }

        String content = request.getContent();
        if (content != null && !content.trim().isBlank()) {
            post.updateContent(content);
        }

        return PostDetailResponse.from(post);
    }
}
