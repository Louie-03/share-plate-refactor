package louie.hanse.shareplate.common.exception.invalid;

public class InvalidLongitudeException extends InvalidException {

    private static final String message = "유효하지 않은 경도입니다.";

    public InvalidLongitudeException() {
        super(message);
    }
}
