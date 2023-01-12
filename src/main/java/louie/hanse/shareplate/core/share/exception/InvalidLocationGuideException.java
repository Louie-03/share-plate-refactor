package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidLocationGuideException extends InvalidException {

    private static final String message = "유효하지 않은 길찾기 값입니다.";

    public InvalidLocationGuideException() {
        super(message);
    }
}
