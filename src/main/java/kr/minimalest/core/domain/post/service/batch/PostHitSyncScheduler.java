package kr.minimalest.core.domain.post.service.batch;

import jakarta.annotation.PreDestroy;
import kr.minimalest.core.domain.post.PostViewKey;
import kr.minimalest.core.domain.post.service.PostHitCounter;
import kr.minimalest.core.domain.post.service.PostHitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostHitSyncScheduler {

    private final PostHitService postHitService;
    private final PostHitCounter postHitCounter;


    @Scheduled(cron = "0 0 0 * * ?")
    public void performOnScheduled() {
        syncHitCountAndClearCache();
    }

    // 서버 종료되기 전에 한 번더 DB 동기화합니다.
    // 캐시 DB가 도입이 안되어있기 때문에, 서버가 종료되면 초기화되는 것이 자연스럽습니다.
    @PreDestroy
    public void performOnShutdown() {
        syncHitCount();
    }

    private void syncHitCountAndClearCache() {
        syncHitCount();
        clearIpCache();
    }

    private void syncHitCount() {
        try {
            postHitCounter.getAllHtiCountFromStorage().forEach(((postViewKey, atomicHitCount) -> {
                postHitService.flushHitCount((PostViewKey) postViewKey);
            }));
            log.info("DB와 조회수 동기화 작업을 완료했습니다.");
        } catch (Exception ex) {
            log.error("DB에 조회수를 동기화하는 과정에서 문제가 발생했습니다!", ex);
        }
    }

    private void clearIpCache() {
        try {
            postHitService.clearIpCache();
            log.info("조회수 IP 캐시 초기화 작업을 완료했습니다.");
        } catch (Exception ex) {
            log.error("조회수 IP 캐시 초기화 과정에서 문제가 발생했습니다!");
        }
    }
}
