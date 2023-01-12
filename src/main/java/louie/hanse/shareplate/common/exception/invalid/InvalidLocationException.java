package louie.hanse.shareplate.common.exception.invalid;

public class InvalidLocationException extends InvalidException {

    private static final String message = "유효하지 않은 주소입니다.";

    public InvalidLocationException() {
        super(message);
    }
}
