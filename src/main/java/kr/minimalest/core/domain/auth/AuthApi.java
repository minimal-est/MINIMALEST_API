package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.auth.dto.GoogleProfileInfo;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static kr.minimalest.core.domain.auth.AuthConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final JwtAuthService jwtAuthService;
    private final GoogleAuthService googleAuthService;

    @Value("${auth.success.redirect-uri}")
    private String AUTH_SUCCESS_REDIRECT_URI;

    @PostMapping("/token")
    public ApiResponse<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        LoginSuccessResponse loginSuccessResponse = jwtAuthService.login(loginRequest, response);
        return ApiResponse.success(loginSuccessResponse);
    }

    @PostMapping("/token/refresh")
    public ApiResponse<?> refresh(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        try {
            jwtAuthService.refreshAccessToken(refreshToken, response);
            return ApiResponse.success("리프레시 토큰이 재발급되었습니다.");
        } catch (IllegalStateException exception) {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "권한이 없습니다!");
        }
    }

    /**
     * <p>구글 계정 인증 성공 시, 호출되는 API입니다. 로그인과 회원인증을 모두 처리합니다.</p>
     * <p>API를 변경하기 위해서는, 구글 OAuth2 클라이언트의 승인된 리다이렉션 URI를 변경해야합니다.</p>
     */
    @GetMapping("/oauth/google")
    public ApiResponse<?> redirectFromGoogleOAuth(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletResponse response
    ) throws IOException {
        if (StringUtils.hasText(error) && error.equals("access_denied")) {
            return ApiResponse.error(HttpStatus.FORBIDDEN, "구글 계정 인증이 거부되었습니다!");
        }

        // 구글에 토큰 획득
        if (StringUtils.hasText(code)) {
            String accessToken = googleAuthService.retrieveToken(code).getAccessToken();
            GoogleProfileInfo googleProfileInfo = googleAuthService.retrieveProfile(accessToken);
            googleAuthService.loginOrJoin(googleProfileInfo, response);
            response.sendRedirect(AUTH_SUCCESS_REDIRECT_URI);
        }

        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 에러가 발생했습니다!");
    }
}
