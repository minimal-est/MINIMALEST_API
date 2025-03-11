package kr.minimalest.core.domain.post.service.batch;

import kr.minimalest.core.domain.post.service.PostHitCounter;
import kr.minimalest.core.domain.post.service.PostHitService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostHitBatch {

    private final PostHitService postHitService;
    private final PostHitCounter postHitCounter;

    // 매일 자정
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveAndClearHitCounts() {
        // DB에 조회수 동기화
        postHitCounter.getHitCountStorage().getAllHitCounts().forEach(((postViewKey, atomicHitCount) -> {
            postHitService.flushHitCount(postViewKey);
        }));

        // IP 캐시 초기화
        postHitService.clearIpChecker();
    }
}
