package kr.minimalest.core.domain.post.service.batch;

import kr.minimalest.core.domain.post.service.PostHitCounter;
import kr.minimalest.core.domain.post.service.PostHitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostHitBatch {

    private final PostHitService postHitService;
    private final PostHitCounter postHitCounter;

    // 매일 3시간마다
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void saveAndClearHitCounts() {
        // DB에 조회수 동기화
        try {
            postHitCounter.getHitCountStorage().getAllHitCounts().forEach(((postViewKey, atomicHitCount) -> {
                postHitService.flushHitCount(postViewKey);
            }));

            // IP 캐시 초기화
            postHitService.clearIpChecker();

            log.info("DB와 조회수 동기화 작업을 완료했습니다.");
        } catch (Exception ex) {
            log.error("DB에 조회수를 동기화시키는 과정에서 문제가 발생했습니다!", ex);
        }
    }
}
