package kr.minimalest.core.domain.file.exception;

public class FileUploaderException extends RuntimeException {

    public FileUploaderException() {
    }

    public FileUploaderException(String message) {
        super(message);
    }

    public FileUploaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploaderException(Throwable cause) {
        super(cause);
    }
}
