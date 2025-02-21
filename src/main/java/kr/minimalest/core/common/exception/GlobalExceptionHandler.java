package kr.minimalest.core.common.exception;

import jakarta.persistence.EntityNotFoundException;
import kr.minimalest.core.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleResponseStatusException(Exception ex) {
        // 기본적으로 모든 오류는 500으로 처리
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        // 스프링 웹에서 기본적으로 응답 코드를 제공해주는 예외들 핸들링(NoResourceFound, HttpMediaTypeException..)
        if (ex instanceof ErrorResponse errorResponse) {
            httpStatus = (HttpStatus) errorResponse.getStatusCode();
        }

        String message = String.format("%s %s", ex.getMessage(), ex.getClass());

        return ApiResponse.error(httpStatus, message);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        // 엔티티를 조회할 수 없을 때
        return ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 모든 Validation 에러 메시지 받기
        String joinedAllErrorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));

        return ApiResponse.error(HttpStatus.BAD_REQUEST, joinedAllErrorMessages);
    }
}

