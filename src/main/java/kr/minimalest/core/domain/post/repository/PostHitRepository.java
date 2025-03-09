package kr.minimalest.core.domain.post.repository;

import kr.minimalest.core.domain.post.PostHit;
import kr.minimalest.core.domain.post.PostViewKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHitRepository extends JpaRepository<PostHit, PostViewKey> {

    @Query("""
            SELECT ph.hit
            FROM PostHit AS ph
            WHERE ph.postViewKey = :postViewKey
    """)
    long getHitCount(PostViewKey postViewKey);
}
