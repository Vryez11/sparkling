package com.dandi.sparkling.post.create;

import com.dandi.sparkling.post.share.Post;
import com.dandi.sparkling.post.share.PostRepository;
import com.dandi.sparkling.user.share.User;
import com.dandi.sparkling.user.share.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CreateEndPoint {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/posts")
    public ResponseEntity<CreateResponse> post(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        CreateResponse response = CreateResponse.from(postRepository.save(post).getId());

        return ResponseEntity
                .created(URI.create("/posts/" + response.getPostId()))
                .body(response);
    }
}
