package louie.hanse.shareplate.common.exception.invalid.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;
import louie.hanse.shareplate.common.exception.invalid.InvalidLatitudeException;
import louie.hanse.shareplate.common.exception.invalid.InvalidLocationException;
import louie.hanse.shareplate.core.keyword.exception.InvalidKeywordContentsException;
import org.springframework.http.HttpStatus;

public enum InvalidExceptionType {

//    KEYWORD
    INVALID_KEYWORD_CONTENTS("KEYWORD001", BAD_REQUEST, InvalidKeywordContentsException.class),

//    COMMON
    INVALID_LOCATION("COMMON001", BAD_REQUEST, InvalidLocationException.class),
    INVALID_LATITUDE("COMMON002", BAD_REQUEST, InvalidLatitudeException.class),
    INVALID_LONGITUDE("COMMON003", BAD_REQUEST, InvalidLocationException.class);

    private final String errorCode;
    private final HttpStatus statusCode;
    private final Class<? extends InvalidException> exception;

    InvalidExceptionType(String errorCode, HttpStatus statusCode, Class<? extends InvalidException> exceptionClass) {
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.exception = exceptionClass;
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
