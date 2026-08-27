package com.dandi.sparkling.repository;

import com.dandi.sparkling.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = "user")
    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void increaseLikeCount(@Param("postId") Long postId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId")
    void decreaseLikeCount(@Param("postId") Long postId);
}
