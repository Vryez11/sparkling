package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.CommentResponse;
import com.dandi.sparkling.dto.CreateCommentRequest;
import com.dandi.sparkling.dto.CreateCommentResponse;
import com.dandi.sparkling.dto.DeleteCommentResponse;
import com.dandi.sparkling.dto.GetCommentListResponse;
import com.dandi.sparkling.entity.Comment;
import com.dandi.sparkling.entity.Post;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.repository.CommentRepository;
import com.dandi.sparkling.repository.PostRepository;
import com.dandi.sparkling.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateCommentResponse createComment(Long userId, Long postId, CreateCommentRequest request) {
        Post post = getActivePost(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .build();

        return CreateCommentResponse.from(commentRepository.save(comment).getId());
    }

    @Transactional(readOnly = true)
    public GetCommentListResponse getCommentList(Long postId) {
        getActivePost(postId);
        List<CommentResponse> comments =
                commentRepository.findAllByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(postId)
                        .stream().map(CommentResponse::from).toList();
        return GetCommentListResponse.from(comments);
    }

    @Transactional
    public DeleteCommentResponse delete(Long userId, Long commentId) {
        Comment comment = getActiveComment(commentId);
        validateAuthor(comment, userId);
        comment.delete();
        return DeleteCommentResponse.from(comment.getId(), comment.getDeletedAt());
    }

    private Post getActivePost(Long postId) {
        return postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
    }

    private Comment getActiveComment(Long commentId) {
        return commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 댓글에 권한이 없습니다.");
        }
    }
}
