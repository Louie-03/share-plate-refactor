package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidHashtagContentsException extends InvalidException {

    private static final String message = "유효하지 않은 해시태그 내용입니다.";

    public InvalidHashtagContentsException() {
        super(message);
    }
}
