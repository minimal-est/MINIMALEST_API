package kr.minimalest.core.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class IpManager<T> {

    private final IpMappingStorageProvider<T> ipStorage;

    public boolean addIp(T key, String clientIp) {
        ipStorage.getStorage().computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet());
        Set<String> ipSet = ipStorage.getStorage().get(key);

        if (!ipSet.contains(clientIp)) {
            ipSet.add(clientIp);
            return true;
        }

        return false;
    }

    public void clear() {
        ipStorage.getStorage().clear();
    }
}
