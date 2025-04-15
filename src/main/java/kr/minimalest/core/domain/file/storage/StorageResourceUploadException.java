package kr.minimalest.core.domain.file.storage;

public class StorageResourceUploadException extends RuntimeException {
    public StorageResourceUploadException() {
        super();
    }

    public StorageResourceUploadException(String message) {
        super(message);
    }

    public StorageResourceUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageResourceUploadException(Throwable cause) {
        super(cause);
    }

    protected StorageResourceUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
