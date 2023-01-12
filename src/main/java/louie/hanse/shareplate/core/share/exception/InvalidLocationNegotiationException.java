package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidLocationNegotiationException extends InvalidException {

    private static final String message = "유효하지 않은 장소 협의 값입니다.";

    public InvalidLocationNegotiationException() {
        super(message);
    }
}
