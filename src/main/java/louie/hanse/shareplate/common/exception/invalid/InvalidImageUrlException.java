package louie.hanse.shareplate.common.exception.invalid;

public class InvalidImageUrlException extends InvalidException {

    private static final String message = "유효하지 않은 이미지 주소입니다.";

    public InvalidImageUrlException() {
        super(message);
    }
}
