package louie.hanse.shareplate.core.keyword.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidKeywordContentsException extends InvalidException {

    private static final String message = "유효하지 않은 키워드 내용입니다.";

    public InvalidKeywordContentsException() {
        super(message);
    }
}
