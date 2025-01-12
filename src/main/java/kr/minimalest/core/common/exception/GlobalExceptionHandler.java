package kr.minimalest.core.common.exception;

import kr.minimalest.core.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handlerException(Exception ex) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
