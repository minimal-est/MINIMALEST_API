package kr.minimalest.core.domain.post.service;

import kr.minimalest.core.domain.file.FileVirtualUrlResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUrlBuilder {

    private final FileVirtualUrlResolver fileVirtualUrlResolver;

    public String build(String key) {
        return fileVirtualUrlResolver.resolve(key);
    }
}
