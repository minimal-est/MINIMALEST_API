package kr.minimalest.core.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.core.domain.auth.dto.GoogleProfileInfo;
import kr.minimalest.core.domain.auth.dto.GoogleTokenInfo;
import kr.minimalest.core.domain.auth.dto.LoginRequest;
import kr.minimalest.core.domain.auth.dto.LoginSuccessResponse;
import kr.minimalest.core.domain.member.MemberService;
import kr.minimalest.core.domain.member.dto.MemberJoinRequest;
import kr.minimalest.core.domain.member.exception.MemberValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAuthService {

    private final RestTemplate restTemplate;
    private final MemberService memberService;
    private final JwtTokenHelper jwtTokenHelper;
    private final AuthResponseHandler authResponseHandler;

    @Value("${auth.oauth.google.client_id}")
    private String CLIENT_ID;

    @Value("${auth.oauth.google.client_secret}")
    private String CLIENT_SECRET;

    @Value("${auth.oauth.google.redirect_uri}")
    private String REDIRECT_URI;

    @Value("${auth.oauth.google.token_uri}")
    private String TOKEN_URI;

    @Value("${auth.oauth.google.resource_uri}")
    private String RESOURCE_URI;

    public LoginSuccessResponse loginOrJoin(GoogleProfileInfo googleProfileInfo, HttpServletResponse response) {
        LoginRequest loginRequest = new LoginRequest(googleProfileInfo.getEmail(), null, AuthType.GOOGLE);
        boolean isNew = false;

        try {
            memberService.validateLogin(loginRequest);
        } catch (MemberValidationException ex) {
            // 존재하지 않는 계정이므로, 회원가입 진행
            join(googleProfileInfo);
            isNew = true;
        }

        // 토큰 발급
        String accessToken = jwtTokenHelper.generateAccessToken(googleProfileInfo.getEmail());
        String refreshToken = jwtTokenHelper.generateRefreshToken(googleProfileInfo.getEmail());

        // 토큰을 응답 헤더 및 쿠키에 설정
        authResponseHandler.setAccessTokenHeader(accessToken, response);
        authResponseHandler.setRefreshTokenCookie(refreshToken, response);

        return new LoginSuccessResponse(googleProfileInfo.getEmail(), isNew);
    }

    public void join(GoogleProfileInfo googleProfileInfo) {
        memberService.joinMember(new MemberJoinRequest(
                googleProfileInfo.getName(),
                googleProfileInfo.getId(),
                googleProfileInfo.getEmail(),
                googleProfileInfo.getPicture(),
                AuthType.GOOGLE
        ));
    }

    /**
     * <p>구글 OAuth2 토큰을 획득합니다.</p>
     * <p>RestTemplate 방식은 쓰레드를 '동기화'시키기 때문에 응답이 올때까지 쓰레드가 블로킹됩니다.
     * 그러므로, 추후 WebClient 등 비동기 방식의 외부 API 호출을 학습한 후 적용하는 게 좋을 듯 합니다.</p>
     * @param code 구글 OAuth2 토큰 획득을 위한 코드입니다..
     * @return 구글에서 응답된 토큰 정보입니다.
     */
    public GoogleTokenInfo retrieveToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = createRequestTokenBody(code);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<GoogleTokenInfo> response = restTemplate.exchange(
            TOKEN_URI, HttpMethod.POST, request, GoogleTokenInfo.class
        );

        return response.getBody();
    }

    /**
     * 구글 리소스 토큰을 사용해 프로필 정보를 획득합니다.
     * @param accessToken 구글 계정 리소스에 접근하기 위한 토큰입니다.
     * @return 구글 계정 프로필 정보입니다.
     */
    public GoogleProfileInfo retrieveProfile(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBearerAuth(accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<GoogleProfileInfo> response = restTemplate.exchange(
                RESOURCE_URI, HttpMethod.GET, request, GoogleProfileInfo.class
        );

        return response.getBody();
    }

    private String createRequestTokenBody(String code) {
        return UriComponentsBuilder.newInstance()
                .queryParam("code", code)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("grant_type", "authorization_code")
                .build()
                .toUriString()
                .substring(1);
    }
}
