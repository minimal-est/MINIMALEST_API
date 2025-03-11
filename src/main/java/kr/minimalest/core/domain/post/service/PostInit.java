package kr.minimalest.core.domain.post.service;

import jakarta.annotation.PostConstruct;
import kr.minimalest.core.domain.post.PostHit;
import kr.minimalest.core.domain.post.PostViewKey;
import kr.minimalest.core.domain.post.repository.PostHitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostInit {

    private final PostHitRepository postHitRepository;
    private final PostHitCounter postHitCounter;

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        // DB의 조회수와 동기화
        List<PostHit> all = postHitRepository.findAll();
        all.forEach((postHit -> {
            PostViewKey postViewKey = postHit.getPostViewKey();
            long hitCount = postHitCounter.getHitCount(postViewKey);
            postHitCounter.setHitCount(postHit.getPostViewKey(), hitCount);
        }));
    }
}
