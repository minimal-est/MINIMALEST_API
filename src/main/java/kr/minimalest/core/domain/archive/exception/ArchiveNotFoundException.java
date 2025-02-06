package kr.minimalest.core.domain.archive.exception;

public class ArchiveNotFoundException extends ArchiveException {

    public ArchiveNotFoundException() {
        super();
    }

    public ArchiveNotFoundException(String message) {
        super(message);
    }

    public ArchiveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchiveNotFoundException(Throwable cause) {
        super(cause);
    }
}
