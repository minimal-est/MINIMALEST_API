package kr.minimalest.core.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(HttpStatus.OK.name(), "success", null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.name(), "success", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(HttpStatus.OK.name(), message, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(HttpStatus.OK.name(), message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus) {
        return new ApiResponse<>(httpStatus.name(), httpStatus.getReasonPhrase(), null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.name(), message, null, LocalDateTime.now());
    }
}
