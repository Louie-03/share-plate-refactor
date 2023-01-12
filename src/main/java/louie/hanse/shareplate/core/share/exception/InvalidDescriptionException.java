package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidDescriptionException extends InvalidException {

    private static final String message = "유효하지 않은 설명입니다.";

    public InvalidDescriptionException() {
        super(message);
    }
}
