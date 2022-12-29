package louie.hanse.shareplate.chat.exception;

import louie.hanse.shareplate.exception.InvalidException;

public class InvalidChatContentsException extends InvalidException {

    public static final String message = "유효하지 않은 채팅 내용입니다.";

    public InvalidChatContentsException() {
        super(message);
    }

}
