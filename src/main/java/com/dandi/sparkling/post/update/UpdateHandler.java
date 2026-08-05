package com.dandi.sparkling.post.update;

import com.dandi.sparkling.post.share.Post;
import com.dandi.sparkling.post.share.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateHandler {

    private final PostRepository postRepository;

    @Transactional
    public UpdateResponse update(Long userId, Long postId, UpdateRequest request) {

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

        return UpdateResponse.from(post);
    }
}
