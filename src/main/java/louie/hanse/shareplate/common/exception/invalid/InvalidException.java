package louie.hanse.shareplate.common.exception.invalid;

public abstract class InvalidException extends RuntimeException {

    private final String message;

    public InvalidException(String message) {
        super(message);
        this.message = message;
    }

}
