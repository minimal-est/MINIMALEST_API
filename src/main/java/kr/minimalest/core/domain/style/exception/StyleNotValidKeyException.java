package kr.minimalest.core.domain.style.exception;

public class StyleNotValidKeyException extends StyleException {
    public StyleNotValidKeyException() {
    }

    public StyleNotValidKeyException(String message) {
        super(message);
    }

    public StyleNotValidKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public StyleNotValidKeyException(Throwable cause) {
        super(cause);
    }
}
