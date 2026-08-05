package com.dandi.sparkling.post.getdetail;

import com.dandi.sparkling.post.share.Post;
import com.dandi.sparkling.post.share.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetDetailEndPoint {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @GetMapping("/posts/{id}")
    public ResponseEntity<GetDetailResponse> post(
            @PathVariable("id") Long postId
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 Post를 찾을 수 없습니다."));

        return ResponseEntity
                .ok(GetDetailResponse.from(post));
    }
}
