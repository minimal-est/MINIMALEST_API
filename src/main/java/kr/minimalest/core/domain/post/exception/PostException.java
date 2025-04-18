package kr.minimalest.core.domain.post.exception;

public class PostException extends RuntimeException {
    public PostException() {
        super();
    }

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostException(Throwable cause) {
        super(cause);
    }
}
