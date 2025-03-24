package kr.minimalest.core.domain.member.exception;

public class MemberConflictException extends MemberException {
    public MemberConflictException() {
        super();
    }

    public MemberConflictException(String message) {
        super(message);
    }

    public MemberConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberConflictException(Throwable cause) {
        super(cause);
    }
}
