package louie.hanse.shareplate.core.share.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidRecruitmentException extends InvalidException {

    private static final String message = "유효하지 않은 모집 인원입니다.";

    public InvalidRecruitmentException() {
        super(message);
    }
}
