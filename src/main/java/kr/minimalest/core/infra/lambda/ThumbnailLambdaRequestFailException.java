package kr.minimalest.core.infra.lambda;

public class ThumbnailLambdaRequestFailException extends ThumbnailLambdaException {
    public ThumbnailLambdaRequestFailException() {
        super();
    }

    public ThumbnailLambdaRequestFailException(String message) {
        super(message);
    }

    public ThumbnailLambdaRequestFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThumbnailLambdaRequestFailException(Throwable cause) {
        super(cause);
    }
}
