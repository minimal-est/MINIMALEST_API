package kr.minimalest.core.domain.post.exception;

public class PostException extends RuntimeException {
    public PostException() {
        super("Post와 관련된 예외입니다.");
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
