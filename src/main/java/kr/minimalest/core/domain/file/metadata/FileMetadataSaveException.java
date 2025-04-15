package kr.minimalest.core.domain.file.metadata;

public class FileMetadataSaveException extends RuntimeException {
    public FileMetadataSaveException() {
        super();
    }

    public FileMetadataSaveException(String message) {
        super(message);
    }

    public FileMetadataSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileMetadataSaveException(Throwable cause) {
        super(cause);
    }
}
