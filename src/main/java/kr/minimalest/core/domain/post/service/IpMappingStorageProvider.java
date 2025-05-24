package kr.minimalest.core.domain.post.service;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class IpMappingStorageProvider<T> {

    private final Map<T, Set<String>> storage = new ConcurrentHashMap<>();

}
