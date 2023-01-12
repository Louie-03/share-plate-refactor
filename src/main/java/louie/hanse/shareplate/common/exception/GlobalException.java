package louie.hanse.shareplate.common.exception;

import louie.hanse.shareplate.common.exception.type.ExceptionType;

public class GlobalException extends RuntimeException {

    private final ExceptionType exceptionType;

    public GlobalException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }

    public int getStatusCode() {
        return exceptionType.getStatusCode().value();
    }
}
