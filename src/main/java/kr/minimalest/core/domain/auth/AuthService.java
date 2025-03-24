package kr.minimalest.core.domain.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenHelper jwtTokenHelper;

    public String generateAccessToken(String email) {
        return jwtTokenHelper.generateAccessToken(email);
    }

    public String generateRefreshToken(String email) {
        return jwtTokenHelper.generateRefreshToken(email);
    }

    public boolean isValidAccessToken(String token) {
        return jwtTokenHelper.isValidToken(token);
    }

    public boolean isValidRefreshToken(String token) {
        return jwtTokenHelper.isValidToken(token);
    }

    public String extractEmailFromToken(String token) {
        return jwtTokenHelper.extractClaims(token)
                .map(Claims::getSubject)
                .orElseThrow(() -> new IllegalArgumentException("토큰에서 Email을 추출할 수 없습니다!"));
    }
}
