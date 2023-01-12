package louie.hanse.shareplate.core.member.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidRefreshTokenException extends InvalidException {

    private static final String message = "유효하지 않는 리프레시 토큰입니다.";

    public InvalidRefreshTokenException() {
        super(message);
    }
}
