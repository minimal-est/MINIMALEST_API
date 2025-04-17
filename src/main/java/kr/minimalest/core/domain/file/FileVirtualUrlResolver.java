package kr.minimalest.core.domain.file;

import org.springframework.util.StringUtils;

/**
 * Spring Bean으로 등록되는 URL Resolver.
 * @see kr.minimalest.core.config.FileVirtualUrlConfig
 */
public class FileVirtualUrlResolver {

    private final String virtualRequestUrl;

    public FileVirtualUrlResolver(String virtualRequestUrl) {
        this.virtualRequestUrl = virtualRequestUrl;
    }

    public String resolve(String key) {
        hasKey(key);
        return virtualRequestUrl + "?key=" + key;
    }

    private static void hasKey(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalStateException("key값이 빈 값 또는 null 입니다!");
        }
    }
}
