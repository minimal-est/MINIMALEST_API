package kr.minimalest.core.domain.post.service;

import jakarta.annotation.PostConstruct;
import kr.minimalest.core.domain.post.PostHit;
import kr.minimalest.core.domain.post.PostViewKey;
import kr.minimalest.core.domain.post.repository.PostHitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostInit {

    private final PostHitRepository postHitRepository;
    private final PostHitCounter postHitCounter;

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        // DB의 조회수와 동기화
        try {
            List<PostHit> all = postHitRepository.findAll();
            all.forEach((postHit -> {
                long hitCount = postHit.getHit();
                postHitCounter.setHitCount(postHit.getPostViewKey(), hitCount);
            }));
            log.info("조회수(HIT)가 성공적으로 동기화 되었습니다.");
        } catch (RuntimeException ex) {
            log.error("조회수(HIT)의 DB를 캐시에 동기화하는데 실패했습니다!", ex);
        }
    }
}
