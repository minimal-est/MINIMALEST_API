package kr.minimalest.core.domain.style.exception;

public class StyleException extends RuntimeException {
    public StyleException() {
        super();
    }

    public StyleException(String message) {
        super(message);
    }

    public StyleException(String message, Throwable cause) {
        super(message, cause);
    }

    public StyleException(Throwable cause) {
        super(cause);
    }
}
