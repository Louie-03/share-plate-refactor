package louie.hanse.shareplate.common.exception.invalid.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;
import louie.hanse.shareplate.core.keyword.exception.InvalidKeywordContentsException;
import org.springframework.http.HttpStatus;

public enum InvalidExceptionType {

//    KEYWORD
    INVALID_KEYWORD_CONTENTS("KEYWORD001", BAD_REQUEST, InvalidKeywordContentsException.class);

    private final String errorCode;
    private final HttpStatus statusCode;
    private final Class<? extends InvalidException> exception;

    InvalidExceptionType(String errorCode, HttpStatus statusCode, Class<? extends InvalidException> exception) {
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.exception = exception;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode.value();
    }

    public Class<? extends InvalidException> getException() {
        return exception;
    }
}
