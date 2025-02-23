package kr.minimalest.core.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.core.domain.auth.JwtHelper;
import kr.minimalest.core.common.annotation.Authenticate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

import static jakarta.servlet.http.HttpServletResponse.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_HEADER_NAME = "Authorization";

    private final JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            Authenticate authenticate = method.getAnnotation(Authenticate.class);

            // @Authenticate 어노테이션이 없을 경우 인증 생략
            if (authenticate == null) return true;

            // 헤더에서 토큰 추출
            String tokenHeader = request.getHeader(TOKEN_HEADER_NAME);

            // 헤더에 인증 토큰이 없다면
            if (!StringUtils.hasText(tokenHeader)) {
                response.sendError(SC_UNAUTHORIZED, "인증 토큰이 존재하지 않습니다.");
                return false;
            }

            String accessToken = tokenHeader.substring(BEARER_PREFIX.length());

            // 유효하지 않는 토큰이라면
            if (!StringUtils.hasText(accessToken) || !jwtHelper.isValidToken(accessToken)) {
                response.sendError(SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
                return false;
            }
        }

        return true;
    }
}
