package kr.minimalest.core.domain.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtHelper jwtHelper;

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
