package kr.minimalest.core.domain.post.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IpChecker<T> {

    private final Map<T, Set<String>> ipSetWithPost = new ConcurrentHashMap<>();

    public boolean addIp(T key, String clientIp) {
        ipSetWithPost.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet());
        Set<String> ipSet = ipSetWithPost.get(key);

        if (!ipSet.contains(clientIp)) {
            ipSet.add(clientIp);
            return true;
        }

        return false;
    }

    public void clear() {
        ipSetWithPost.clear();
    }
}
