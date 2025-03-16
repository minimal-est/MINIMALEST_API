package kr.minimalest.core.domain.post.service;

import jakarta.servlet.http.HttpServletRequest;

public class PostThumbnailHostResolver {

    public static String getServerHost(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
