package kr.minimalest.core.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class ServerHostResolver {

    public static String getServerHost(HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto");
        String host = request.getHeader("Host");
        if (!StringUtils.hasText(scheme)) {
            scheme = request.getScheme();
        }
        if (!StringUtils.hasText(host)) {
            host = request.getServerName() + ":" + request.getServerPort();
        }
        return scheme + "://" + host;
    }
}
