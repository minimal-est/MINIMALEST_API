package kr.minimalest.core.infra.lambda;

public class ThumbnailLambdaException extends RuntimeException {
    public ThumbnailLambdaException() {
    }

    public ThumbnailLambdaException(String message) {
        super(message);
    }

    public ThumbnailLambdaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThumbnailLambdaException(Throwable cause) {
        super(cause);
    }
}
