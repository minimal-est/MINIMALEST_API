package kr.minimalest.core.domain.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FileVirtualUrlResolver {

    @Value("${file.proxy.url}")
    private String VIRTUAL_REQUEST;

    public String resolve(String key) {
        hasKey(key);
        return VIRTUAL_REQUEST + "?key=" + key;
    }

    private static void hasKey(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalStateException("key값이 빈 값 또는 null 입니다!");
        }
    }
}
