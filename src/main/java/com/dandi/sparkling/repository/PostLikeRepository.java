package com.dandi.sparkling.repository;

import com.dandi.sparkling.entity.Post;
import com.dandi.sparkling.entity.PostLike;
import com.dandi.sparkling.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
