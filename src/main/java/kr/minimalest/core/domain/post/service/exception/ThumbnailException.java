package kr.minimalest.core.domain.post.service.exception;

public class ThumbnailException extends RuntimeException {
    public ThumbnailException() {
        super();
    }

    public ThumbnailException(String message) {
        super(message);
    }

    public ThumbnailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThumbnailException(Throwable cause) {
        super(cause);
    }
}
