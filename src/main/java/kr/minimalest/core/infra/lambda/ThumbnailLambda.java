package kr.minimalest.core.infra.lambda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class ThumbnailLambda {
    private final RestTemplate restTemplate;

    @Value("${aws.lambda.thumbnail-generate-url}")
    private String TRIGGER_URL;

    /**
     * 썸네일을 생성하기 위한 메소드입니다. imageKey와 함께 TRIGGER_URL에 해당하는 람다함수를 실행시킵니다.
     * @param imageKey 스토리지에 저장된 이미지의 key ex) image/{UUID}.png
     * @return 썸네일로 변환된 이미지의 key ex) thumbnail/{key}.png
     */
    public String generateThumbnail(String imageKey) {
        Map<String, String> requestBody = Map.of("key", imageKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> result = restTemplate.exchange(
                    TRIGGER_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (!StringUtils.hasText(result.getBody())) {
                throw new ThumbnailLambdaException("썸네일 생성 람다 함수 응답값이 비어있습니다!");
            }

            // "result" -> result
            return result.getBody().substring(1, result.getBody().length() - 1);

        } catch (Exception ex) {
            throw new ThumbnailLambdaRequestFailException("썸네일 생성 람다 함수 요청에 실패했습니다!", ex);
        }
    }
}
