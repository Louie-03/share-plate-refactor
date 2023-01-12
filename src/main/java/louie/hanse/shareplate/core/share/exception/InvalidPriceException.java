package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidPriceException extends InvalidException {

    private static final String message = "유효하지 않은 가격입니다.";

    public InvalidPriceException() {
        super(message);
    }
}
