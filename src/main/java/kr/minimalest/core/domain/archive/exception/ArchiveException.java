package kr.minimalest.core.domain.archive.exception;

public class ArchiveException extends RuntimeException {

    public ArchiveException() {
        super();
    }

    public ArchiveException(String message) {
        super(message);
    }

    public ArchiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchiveException(Throwable cause) {
        super(cause);
    }
}
