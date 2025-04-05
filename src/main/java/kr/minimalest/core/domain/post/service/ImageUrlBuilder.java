package kr.minimalest.core.domain.post.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.common.utils.ServerHostResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUrlBuilder {

    private final HttpServletRequest request;

    @Value("${file.proxy.url}")
    private String proxyUrl;

    public String build(String key) {
        String host = ServerHostResolver.getServerHost(request);
        return host + proxyUrl + "?key=" + key;
    }
}
