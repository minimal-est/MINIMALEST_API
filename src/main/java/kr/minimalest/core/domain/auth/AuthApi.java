package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.core.domain.member.MemberService;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ApiResponse<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        // 회원 검증
        memberService.validateLogin(loginRequest);

        // Token 발급
        String accessToken = jwtService.generateAccessToken(loginRequest.getEmail());
        String refreshToken = jwtService.generateRefreshToken(loginRequest.getEmail());

        // Access Token 헤더 추가
        setAccessTokenHeader(accessToken, response);

        // Refresh Token 쿠키 저장
        Cookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

        return ApiResponse.success(new LoginSuccessResponse(loginRequest.getEmail()));
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (!StringUtils.hasText(refreshToken) || jwtService.isValidToken(refreshToken)) {
            String subject = jwtService.extractClaims(refreshToken).get().getSubject();

            // 새로운 Access Token 발급 및 헤더 추가
            String newAccessToken = jwtService.generateAccessToken(subject);
            setAccessTokenHeader(newAccessToken, response);

            return ApiResponse.success();
        } else {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "올바르지 않은 Refresh Token 입니다.");
        }
    }

    private void setAccessTokenHeader(String accessToken, HttpServletResponse targetResponse) {
        targetResponse.addHeader("Minimalest-Access-Token", accessToken);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        return createCookie("refreshToken", refreshToken, jwtService.REFRESH_EXPIRATION / 1000);
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
