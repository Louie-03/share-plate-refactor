package louie.hanse.shareplate.exception;

public class InvalidException extends RuntimeException {

    private final String message;

    public InvalidException(String message) {
        super(message);
        this.message = message;
    }

}
