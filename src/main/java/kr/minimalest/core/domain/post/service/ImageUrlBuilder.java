package kr.minimalest.core.domain.post.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.common.utils.ServerHostResolver;
import kr.minimalest.core.domain.file.FileVirtualUrlResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUrlBuilder {

    private final FileVirtualUrlResolver fileVirtualUrlResolver;
    private final ObjectProvider<HttpServletRequest> requestProvider;

    public String build(String key) {
        HttpServletRequest request = requestProvider.getObject();
        String host = ServerHostResolver.getServerHost(request);
        return host + fileVirtualUrlResolver.resolve(key);
    }
}
