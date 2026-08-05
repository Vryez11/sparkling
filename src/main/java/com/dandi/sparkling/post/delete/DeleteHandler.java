package com.dandi.sparkling.post.delete;

import com.dandi.sparkling.post.share.Post;
import com.dandi.sparkling.post.share.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteHandler {

    private final PostRepository postRepository;

    @Transactional
    public DeleteResponse delete(Long userId, Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 Post를 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("해당 게시글에 권한이 없습니다.");
        }

        if (post.getDeletedAt() != null) {
            throw new RuntimeException("이미 삭제된 게시글 입니다.");
        }

        post.delete();

        return DeleteResponse.from(post.getId(), post.getDeletedAt());
    }
}
