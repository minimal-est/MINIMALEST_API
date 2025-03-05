package kr.minimalest.core.domain.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static kr.minimalest.core.domain.auth.AuthConstants.ACCESS_TOKEN_HEADER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtHelper jwtHelper;

    public String refreshAccessTokenWithHeader(String refreshToken, HttpServletResponse response) {
        String newAccessToken = refreshAccessToken(refreshToken);
        response.setHeader(ACCESS_TOKEN_HEADER, newAccessToken);
        return newAccessToken;
    }

    public String refreshAccessToken(String refreshToken) {
        if (StringUtils.hasText(refreshToken) && isValidRefreshToken(refreshToken)) {
            String email = extractEmailFromToken(refreshToken);
            return generateAccessToken(email);
        }

        throw new IllegalArgumentException("refresh token이 올바르지 않습니다!");
    }

    public String generateAccessToken(String email) {
        return jwtHelper.generateAccessToken(email);
    }

    public String generateRefreshToken(String email) {
        return jwtHelper.generateRefreshToken(email);
    }

    public boolean isValidAccessToken(String token) {
        return jwtHelper.isValidToken(token);
    }

    public boolean isValidRefreshToken(String token) {
        return jwtHelper.isValidToken(token);
    }

    public String extractEmailFromToken(String token) {
        return jwtHelper.extractClaims(token)
                .map(Claims::getSubject)
                .orElseThrow(() -> new IllegalArgumentException("토큰에서 Email을 추출할 수 없습니다!"));
    }
}
