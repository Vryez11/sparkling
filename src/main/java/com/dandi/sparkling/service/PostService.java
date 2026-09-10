package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.*;
import com.dandi.sparkling.entity.Post;
import com.dandi.sparkling.entity.PostLike;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.repository.PostLikeRepository;
import com.dandi.sparkling.repository.PostRepository;
import com.dandi.sparkling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public CreatePostResponse createPost(Long userId, CreatePostRequest request) {

        User user = getActiveUser(userId);

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .count(0)
                .build();

        return CreatePostResponse.from(postRepository.save(post).getId());
    }

    @Transactional(readOnly = true)
    public GetPostListResponse getPostList(int page, int size) {

        Page<Post> postPage = postRepository.findAllByDeletedAtIsNull(toPageRequest(page, size));

        return toPostListResponse(postPage);
    }

    @Transactional(readOnly = true)
    public GetPostListResponse getMyPostList(Long userId, int page, int size) {

        Page<Post> postPage = postRepository.findAllByUserIdAndDeletedAtIsNull(userId, toPageRequest(page, size));

        return toPostListResponse(postPage);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse postDetail(Long postId) {

        Post post = getActivePost(postId);

        return PostDetailResponse.from(post);
    }

    @Transactional
    public PostDetailResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {

        Post post = getActivePost(postId);
        validateAuthor(post, userId);

        String title = request.getTitle();
        if (title != null && !title.isBlank()) {
            post.updateTitle(title);
        }

        String content = request.getContent();
        if (content != null && !content.isBlank()) {
            post.updateContent(content);
        }

        return PostDetailResponse.from(post);
    }

    @Transactional
    public DeletePostResponse delete(Long userId, Long postId) {

        Post post = getActivePost(postId);
        validateAuthor(post, userId);

        post.delete();

        return DeletePostResponse.from(post.getId(), post.getDeletedAt());
    }

    @Transactional
    public PostLikeResponse likePost(Long userId, Long postId) {

        User existingUser = getActiveUser(userId);
        Post existingPost = getActivePost(postId);

        Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(existingPost, existingUser);

        if (postLike.isPresent()) {
            throw new RuntimeException("이미 좋아요를 누른 게시글입니다.");
        }

        postLikeRepository.save(PostLike.builder()
                .user(existingUser)
                .post(existingPost)
                .build());

        postRepository.increaseLikeCount(postId);

        Post refreshed = getActivePost(postId);

        return PostLikeResponse.from(refreshed.getId(), refreshed.getLikeCount());
    }

    @Transactional
    public PostLikeResponse unlikePost(Long userId, Long postId) {

        User existingUser = getActiveUser(userId);
        Post existingPost = getActivePost(postId);

        PostLike postLike = postLikeRepository.findByPostAndUser(existingPost, existingUser)
                .orElseThrow(() -> new RuntimeException("좋아요를 누르지 않은 게시글입니다."));

        postLikeRepository.delete(postLike);

        postRepository.decreaseLikeCount(postId);

        Post refreshed = getActivePost(postId);

        return PostLikeResponse.from(refreshed.getId(), refreshed.getLikeCount());
    }

    private PageRequest toPageRequest(int page, int size) {

        if (page < 0) {
            page = 0;
        }
        if (size < 1) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
    }

    private GetPostListResponse toPostListResponse(Page<Post> postPage) {

        List<PostResponse> posts = postPage.getContent()
                .stream()
                .map(PostResponse::from)
                .toList();

        PageInfoResponse pageInfo = PageInfoResponse.from(
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.hasNext()
        );

        return GetPostListResponse.from(posts, pageInfo);
    }

    private User getActiveUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
    }

    private Post getActivePost(Long postId) {

        return postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
    }

    private void validateAuthor(Post post, Long userId) {

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 게시글에 권한이 없습니다.");
        }
    }
}
