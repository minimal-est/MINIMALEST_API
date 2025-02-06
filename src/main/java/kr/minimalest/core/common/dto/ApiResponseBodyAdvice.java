package kr.minimalest.core.common.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 실제 응답 코드와 ApiResponse 응답 코드를 통일시킵니다.
 */
@Slf4j
@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ApiResponse<?> responseBody = (ApiResponse<?>) body;

        // ApiResponse 응답 코드를 실제 응답 코드로 설정
        int statusCode = responseBody.getStatusCode();
        response.setStatusCode(HttpStatusCode.valueOf(statusCode));

        return body;
    }
}
