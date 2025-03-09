package kr.minimalest.core.domain.post.exception;

public class PostHitException extends PostException {
    public PostHitException() {
        super();
    }

    public PostHitException(String message) {
        super(message);
    }

    public PostHitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostHitException(Throwable cause) {
        super(cause);
    }
}
