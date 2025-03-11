package kr.minimalest.core.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.domain.post.PostHit;
import kr.minimalest.core.domain.post.PostViewKey;
import kr.minimalest.core.domain.post.repository.PostHitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostHitService {

    private final PostHitCounter postHitCounter;
    private final PostHitRepository postHitRepository;
    private final IpChecker<PostViewKey> ipChecker;

    @Transactional
    public void createPostHit(PostViewKey postViewKey) {
        PostHit postHit = PostHit.create(postViewKey, 0L);
        postHitRepository.save(postHit);
    }

    @Transactional
    public void flushHitCount(PostViewKey postViewKey) {
        long hitCount = postHitCounter.getHitCount(postViewKey);
        PostHit postHit = postHitRepository.findById(postViewKey)
                .orElseThrow(() -> new EntityNotFoundException("포스트의 조회수 엔티티가 존재하지 않습니다!"));
        postHit.updateHit(hitCount);
    }

    public void incrementHitCount(PostViewKey postViewKey, String clientIp) {
        if (ipChecker.addIp(postViewKey, clientIp)) {
            postHitCounter.incrementHitCount(postViewKey);
        }
    }

    public long getHitCount(PostViewKey postViewKey) {
        return postHitCounter.getHitCount(postViewKey);
    }

    public void clearIpChecker() {
        ipChecker.clear();
    }
}
