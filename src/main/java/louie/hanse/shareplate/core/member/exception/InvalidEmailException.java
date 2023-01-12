package louie.hanse.shareplate.core.member.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidEmailException extends InvalidException {

    private static final String message = "유효하지 않은 이메일입니다.";

    public InvalidEmailException() {
        super(message);
    }
}
