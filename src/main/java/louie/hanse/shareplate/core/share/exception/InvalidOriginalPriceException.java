package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidOriginalPriceException extends InvalidException {

    private static final String message = "유효하지 않은 원가입니다.";

    public InvalidOriginalPriceException() {
        super(message);
    }
}
