package kr.minimalest.core.common.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.core.domain.auth.JwtService;
import kr.minimalest.core.common.annotation.AuthenticatedMemberEmail;
import kr.minimalest.core.domain.auth.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedMemberEmailArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMemberEmail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String tokenHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(BEARER_PREFIX)) {
            String accessToken = tokenHeader.substring(BEARER_PREFIX.length());
            if (jwtService.isValidToken(accessToken)) {
                return jwtService.extractClaims(accessToken).get().getSubject();
            }
        }

        throw new UnAuthorizedException("인증 정보가 유효하지 않습니다!");
    }
}
