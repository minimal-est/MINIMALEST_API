package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.minimalest.core.common.dto.ApiResponse;
import kr.minimalest.core.domain.auth.dto.GoogleProfileInfo;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;
import kr.minimalest.core.domain.member.exception.MemberConflictException;
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
     * <p>구글 계정 인증 성공 시, 호출되는 API입니다. 로그인과 회원인증을 모두 처리합니다.
     * API를 변경하기 위해서는, 구글 OAuth2 클라이언트의 승인된 리다이렉션 URI를 변경해야합니다.</p>
     * <p>성공 - {redirect_uri}?isNew=(true, false) : isNew 값은 사용자가 새로 가입한 회원인지 여부입니다.</p>
     * <p>거부 - {redirect_uri}?error=access_denied</p>
     * <P>가입할 수 없는 회원 - {redirect_uri}?error=conflict</P>
     */
    @GetMapping("/oauth/google")
    public void redirectFromGoogleOAuth(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletResponse response
    ) throws IOException {

        StringBuilder redirectUriSb = new StringBuilder(AUTH_SUCCESS_REDIRECT_URI);

        if (StringUtils.hasText(error)) {
            // 계정 인증 실패 시 error 파라미터 담아서 제공
            redirectUriSb.append("?error=").append(error);
        }

        // 구글에 토큰 획득
        if (StringUtils.hasText(code)) {
            String accessToken = googleAuthService.retrieveToken(code).getAccessToken();
            GoogleProfileInfo googleProfileInfo = googleAuthService.retrieveProfile(accessToken);

            try {
                LoginSuccessResponse loginSuccessResponse = googleAuthService.loginOrJoin(googleProfileInfo, response);
                // 클라이언트에게 구글 인증 상태를 제공합니다.
                // isNew : 새로 가입된 회원인지 여부 (true, false)
                redirectUriSb.append("?isNew=").append(loginSuccessResponse.isNew());
            } catch (MemberConflictException ex) {
                // 이미 기존의 이메일로 가입되어있을 시
                redirectUriSb.append("?error=conflict");
            }
        }

        response.sendRedirect(redirectUriSb.toString());
    }
}
