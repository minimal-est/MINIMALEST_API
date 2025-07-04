package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.post.PostViewKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostHitCounter {

    private final HitCountStorage<PostViewKey> hitCountStorage;

    public void incrementHitCount(PostViewKey key) {
        hitCountStorage.increment(key);
    }

    public long getHitCount(PostViewKey key) {
        return hitCountStorage.get(key);
    }

    public void setHitCount(PostViewKey key, long hitCount) {
        hitCountStorage.set(key, hitCount);
    }

    public HitCountStorage<PostViewKey> getHitCountStorage() {
        return hitCountStorage;
    }

    // 편의 메소드
    public Map<?, AtomicLong> getAllHtiCountFromStorage() {
        return hitCountStorage.getAllHitCounts();
    }

    public void clearStorage() {
        hitCountStorage.clear();
    }
}
