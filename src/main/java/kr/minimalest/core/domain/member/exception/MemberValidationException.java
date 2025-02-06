package kr.minimalest.core.domain.member.exception;

public class MemberValidationException extends MemberException {
    public MemberValidationException() {
        super();
    }

    public MemberValidationException(String message) {
        super(message);
    }

    public MemberValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberValidationException(Throwable cause) {
        super(cause);
    }
}
