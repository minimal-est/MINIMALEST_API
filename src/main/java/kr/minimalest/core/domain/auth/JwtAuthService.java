package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;
import kr.minimalest.core.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthService {
    private final MemberService memberService;
    private final AuthService authService;
    private final AuthResponseHandler authResponseHandler;

    public LoginSuccessResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        // 회원 검증
        memberService.validateLogin(loginRequest);

        // Token 발급
        String accessToken = authService.generateAccessToken(loginRequest.getEmail());
        String refreshToken = authService.generateRefreshToken(loginRequest.getEmail());

        // Access Token 헤더 추가
        authResponseHandler.setAccessTokenHeader(accessToken, response);

        // Refresh Token 쿠키 저장
        authResponseHandler.setRefreshTokenCookie(refreshToken, response);

        return new LoginSuccessResponse(loginRequest.getEmail());
    }

    public void refreshAccessToken(String refreshToken, HttpServletResponse response) {
        if (authService.isValidRefreshToken(refreshToken)) {
            // 새로운 Access Token 발급
            String email = authService.extractEmailFromToken(refreshToken);
            String newAccessToken = authService.generateAccessToken(email);

            // 새로운 Access Token 헤더 추가
            authResponseHandler.setAccessTokenHeader(newAccessToken, response);
        }
    }

    public AuthType getAuthType() {
        return AuthType.JWT;
    }
}
