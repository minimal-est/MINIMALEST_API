package kr.minimalest.core.domain.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileVirtualUrlResolver {

    @Value("${file.proxy.url}")
    private String VIRTUAL_REQUEST;

    public String resolve(String key) {
        return VIRTUAL_REQUEST + "?key=" + key;
    }
}
