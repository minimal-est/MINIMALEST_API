package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static kr.minimalest.core.domain.auth.AuthConstants.*;

@Component
@RequiredArgsConstructor
public class AuthResponseHandler {

    private final JwtTokenHelper jwtTokenHelper;

    public void setAccessTokenHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_HEADER, accessToken);
    }

    public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = createTokenCookie(refreshToken);
        response.addCookie(cookie);
    }

    private Cookie createTokenCookie(String value) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtTokenHelper.REFRESH_EXPIRATION / 1000);
        return cookie;
    }
}
