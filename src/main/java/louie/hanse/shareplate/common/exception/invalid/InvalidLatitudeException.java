package louie.hanse.shareplate.common.exception.invalid;

public class InvalidLatitudeException extends InvalidException {

    private static final String message = "유효하지 않은 위도값입니다.";

    public InvalidLatitudeException() {
        super(message);
    }
}
