package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidPriceNegotiationException extends InvalidException {

    private static final String message = "유효하지 않은 가격 협의 값입니다.";

    public InvalidPriceNegotiationException() {
        super(message);
    }
}
