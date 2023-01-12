package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidTitleException extends InvalidException {

    private static final String message = "유효하지 않은 제목입니다.";

    public InvalidTitleException() {
        super(message);
    }
}
