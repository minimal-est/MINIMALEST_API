package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.minimalest.core.domain.member.MemberService;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static kr.minimalest.core.domain.auth.AuthConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final MemberService memberService;
    private final AuthService authService;
    private final JwtHelper jwtHelper;

    @PostMapping("/login")
    public ApiResponse<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        // 회원 검증
        memberService.validateLogin(loginRequest);

        // Token 발급
        String accessToken = authService.generateAccessToken(loginRequest.getEmail());
        String refreshToken = authService.generateRefreshToken(loginRequest.getEmail());

        // Access Token 헤더 추가
        setAccessTokenHeader(accessToken, response);

        // Refresh Token 쿠키 저장
        Cookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

        return ApiResponse.success(new LoginSuccessResponse(loginRequest.getEmail()));
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refresh(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        try {
            authService.refreshAccessTokenWithHeader(refreshToken, response);
            return ApiResponse.success("리프레시 토큰이 재발급되었습니다.");
        } catch (IllegalStateException exception) {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "권한이 없습니다!");
        }
    }

    private void setAccessTokenHeader(String accessToken, HttpServletResponse targetResponse) {
        targetResponse.addHeader(ACCESS_TOKEN_HEADER, accessToken);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        return createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, jwtHelper.REFRESH_EXPIRATION / 1000);
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
