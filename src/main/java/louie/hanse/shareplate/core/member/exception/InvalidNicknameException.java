package louie.hanse.shareplate.core.member.exception;

import louie.hanse.shareplate.common.exception.invalid.InvalidException;

public class InvalidNicknameException extends InvalidException {

    private static final String message = "유효하지 않은 회원 이름입니다.";

    public InvalidNicknameException() {
        super(message);
    }
}
