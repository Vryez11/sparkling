package com.dandi.sparkling.post.getlist;

import com.dandi.sparkling.post.share.Post;
import com.dandi.sparkling.post.share.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetListEndPoint {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @GetMapping("/posts")
    public ResponseEntity<GetListResponse> postList() {

        List<PostSummary> posts = postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(p -> PostSummary.from(p.getId(), p.getTitle(), p.getUser().getNickname(), p.getCreatedAt()))
                .toList();

        return ResponseEntity
                .ok(GetListResponse.from(posts));
    }
}
