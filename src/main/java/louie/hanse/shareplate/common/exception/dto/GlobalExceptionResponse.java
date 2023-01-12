package louie.hanse.shareplate.common.exception.dto;

import lombok.Getter;
import louie.hanse.shareplate.common.exception.GlobalException;

@Getter
public class GlobalExceptionResponse {
    private String errorCode;
    private String message;

    public GlobalExceptionResponse(GlobalException globalException) {
        this.errorCode = globalException.getErrorCode();
        this.message = globalException.getMessage();
    }
}
