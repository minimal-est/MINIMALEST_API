package kr.minimalest.core.infra.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("썸네일 생성 람다 클래스")
class ThumbnailLambdaTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ThumbnailLambda thumbnailLambda;

    private final String TEST_IMAGE_KEY = "image/sample.png";
    private final String TEST_THUMBNAIL_KEY = "thumbnail/image/sample.png";
    private final String MOCK_TRIGGER_URL = "https://mock-trigger-url.good";

    @BeforeEach
    void setUp() throws Exception {
        // 람다 함수 트리거 URL을 주입시킵니다.
        Field triggerUrlField = ThumbnailLambda.class.getDeclaredField("TRIGGER_URL");
        triggerUrlField.setAccessible(true);
        triggerUrlField.set(thumbnailLambda, MOCK_TRIGGER_URL);
    }

    @Test
    @DisplayName("람다 함수 썸네일 생성 요청 및 응답 성공")
    void generateThumbnail_success() {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>("\"" + TEST_THUMBNAIL_KEY + "\"", HttpStatus.OK);
        Map<String, String> requestBody = Map.of("key", TEST_IMAGE_KEY);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        when(restTemplate.exchange(
                eq(MOCK_TRIGGER_URL),
                eq(HttpMethod.POST),
                eq(request),
                eq(String.class)
        )).thenReturn(mockResponse);

        // when
        String result = thumbnailLambda.generateThumbnail(TEST_IMAGE_KEY);

        // then
        assertThat(result).isEqualTo(TEST_THUMBNAIL_KEY);
    }

    @Test
    @DisplayName("람다 응답이 빈 값이면 예외 발생")
    void generateThumbnail_emptyResponse_throwsException() {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>("", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(MOCK_TRIGGER_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        // when & then
        assertThrows(ThumbnailLambdaException.class, () -> {
            thumbnailLambda.generateThumbnail(TEST_IMAGE_KEY);
        });
    }

    @Test
    @DisplayName("람다 함수 요청에 실패 했을 때 예외 발생")
    void generateThumbnail_requestFail_throwsException() {
        // given
        when(restTemplate.exchange(
                any(),
                any(),
                any(),
                eq(String.class)
        )).thenThrow(ThumbnailLambdaRequestFailException.class);

        // when & then
        assertThrows(ThumbnailLambdaRequestFailException.class, () -> {
            thumbnailLambda.generateThumbnail(TEST_IMAGE_KEY);
        });
    }
}
