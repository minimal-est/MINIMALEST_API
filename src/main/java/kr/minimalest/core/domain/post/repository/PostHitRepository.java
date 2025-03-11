package kr.minimalest.core.domain.post.repository;

import kr.minimalest.core.domain.post.PostHit;
import kr.minimalest.core.domain.post.PostViewKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHitRepository extends JpaRepository<PostHit, PostViewKey> {
}
