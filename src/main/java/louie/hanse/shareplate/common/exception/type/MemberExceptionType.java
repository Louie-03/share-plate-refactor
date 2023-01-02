package louie.hanse.shareplate.common.exception.type;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements ExceptionType {
    MEMBER_NOT_FOUND("MEMBER001", "존재하지 않는 회원입니다.", BAD_REQUEST),
    NOT_SUPPORT_IMAGE_TYPE("MEMBER002", "회원 이미지 형식의 파일이 아닙니다.", BAD_REQUEST),
    EMPTY_MEMBER_INFO("MEMBER003", "요청한 회원정보 값이 비어있습니다.", BAD_REQUEST),
    EMPTY_LOCATION("MEMBER004", "요청한 위치정보 값이 비어있습니다.", BAD_REQUEST),
    NOT_LOGIN_MEMBER("MEMBER005", "로그인되지 않은 회원입니다.", UNAUTHORIZED),
    PROFILE_IMAGE_LIMIT_EXCEEDED("MEMBER006", "이미지 1개를 초과하였습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    MemberExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatusCode() {
        return httpStatusCode;
    }
}
