package kr.minimalest.core.domain.post.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class HitCountStorage<T> {

    private final Map<T, AtomicLong> hitCounts = new ConcurrentHashMap<>();

    // 조회수 증가
    public void increment(T key) {
        hitCounts.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();
    }

    // 조회수 조회
    public long get(T key) {
        return hitCounts.getOrDefault(key, new AtomicLong(0)).get();
    }

    public void set(T key, long value) {
        hitCounts.computeIfAbsent(key, k -> new AtomicLong(0)).set(value);
    }

    public Map<T, AtomicLong> getAllHitCounts() {
        return hitCounts;
    }

    public void clear() {
        hitCounts.clear();
    }
}
