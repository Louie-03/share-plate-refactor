package louie.hanse.shareplate.common.exception.type;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    String getErrorCode();

    String getMessage();

    HttpStatus getStatusCode();
}
