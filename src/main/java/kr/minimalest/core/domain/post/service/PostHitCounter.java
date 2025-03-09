package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.post.PostViewKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostHitCounter {

    private final HitCountStorage<PostViewKey> hitCountStorage;

    public void incrementHitCount(PostViewKey key) {
        log.info("Post: {}, incrementHitCount!", key);
        hitCountStorage.increment(key);
    }

    public long getHitCount(PostViewKey key) {
        log.info("Post: {}, HitCount: {}", key, hitCountStorage.get(key));
        return hitCountStorage.get(key);
    }

    public void setHitCount(PostViewKey key, long hitCount) {
        hitCountStorage.set(key, hitCount);
    }

    public HitCountStorage<PostViewKey> getHitCountStorage() {
        return hitCountStorage;
    }

    public void clearStorage() {
        hitCountStorage.clear();
    }
}
