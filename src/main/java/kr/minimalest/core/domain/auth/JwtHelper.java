package kr.minimalest.core.domain.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtHelper {

    @Value("${jwt.access-expiration}")
    public int ACCESS_EXPIRATION; // Default 30분

    @Value("${jwt.refresh-expiration}")
    public int REFRESH_EXPIRATION; // Default 7일

    @Value("${jwt.secret-key}")
    private String SECRET_CODE;

    // JWT 시그니쳐를 위한 키
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // SECRET_CODE가 주입받으므로, 주입받은 후에 SecretKey로 암호화합니다.
        this.secretKey = Keys.hmacShaKeyFor(SECRET_CODE.getBytes());
    }

    // Access Token 생성
    public String generateAccessToken(String email) {
        return generateToken(email, System.currentTimeMillis() + ACCESS_EXPIRATION);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String email) {
        return generateToken(email, System.currentTimeMillis() + REFRESH_EXPIRATION);
    }

    // 토큰 검증
    public boolean isValidToken(String token) {
        Optional<Claims> optionalClaims = extractClaims(token);
        return optionalClaims.isPresent();
    }

    // Claims 파싱
    public Optional<Claims> extractClaims(String token) {
        Optional<Claims> optionalClaims;

        try {
            // 올바른 토큰일 경우
            optionalClaims = Optional.of(Jwts.parser()
                    //.clockSkewSeconds(CLOCK_SKEW_SECONDS)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (JwtException e) {
            // 올바르지 않은 토큰일 경우
            optionalClaims = Optional.empty();
        }

        return optionalClaims;
    }

    private String generateToken(String subject, long expiration) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(expiration))
                .signWith(secretKey)
                .compact();
    }
}
