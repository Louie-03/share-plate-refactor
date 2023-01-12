package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidClosedDateTimeException extends InvalidException {

    private static final String message = "유효하지 않은 마감 시간입니다.";

    public InvalidClosedDateTimeException() {
        super(message);
    }
}
