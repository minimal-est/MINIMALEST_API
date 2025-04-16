package kr.minimalest.core.domain.file.storage;

public class StorageResourceDownloadException extends RuntimeException {
    public StorageResourceDownloadException() {
        super();
    }

    public StorageResourceDownloadException(String message) {
        super(message);
    }

    public StorageResourceDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageResourceDownloadException(Throwable cause) {
        super(cause);
    }
}
