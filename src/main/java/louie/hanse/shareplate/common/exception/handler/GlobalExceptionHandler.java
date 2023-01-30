package louie.hanse.shareplate.common.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.invalid.InvalidException;
import louie.hanse.shareplate.common.exception.invalid.type.InvalidExceptionType;
import louie.hanse.shareplate.common.exception.type.AuthExceptionType;
import louie.hanse.shareplate.common.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.common.exception.type.EntryExceptionType;
import louie.hanse.shareplate.common.exception.type.ExceptionType;
import louie.hanse.shareplate.common.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.common.exception.type.MemberExceptionType;
import louie.hanse.shareplate.common.exception.type.NotificationExceptionType;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;
import louie.hanse.shareplate.common.exception.type.WishExceptionType;
import louie.hanse.shareplate.common.exception.dto.GlobalExceptionResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public GlobalExceptionResponse globalExceptionResponse(GlobalException globalException,
        HttpServletResponse response) {
        response.setStatus(globalException.getStatusCode());
        return new GlobalExceptionResponse(globalException);
    }

    @ExceptionHandler({ValidationException.class, BindException.class,
        MethodArgumentNotValidException.class, TypeMismatchException.class})
    public ResponseEntity<GlobalExceptionResponse> exceptionResponse(Exception exception) {
        String message = getMessage(exception);
        ExceptionType exceptionType = findExceptionType(message);
        return ResponseEntity.status(exceptionType.getStatusCode())
            .body(new GlobalExceptionResponse(new GlobalException(exceptionType)));
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<GlobalExceptionResponse> invalidExceptionResponse(InvalidException exception) {
        InvalidExceptionType invalidExceptionType = findInvalidExceptionType(exception);
        return ResponseEntity.status(invalidExceptionType.getStatusCode())
            .body(new GlobalExceptionResponse(invalidExceptionType.getErrorCode(),
                exception.getMessage()));
    }

    private ExceptionType findExceptionType(String message) {
        List<ExceptionType> exceptionTypes = createExceptionTypes();
        for (ExceptionType exceptionType : exceptionTypes) {
            if (message.contains(exceptionType.getMessage())) {
                return exceptionType;
            }
        }
//        TODO : ExceptionType을 찾지 못하는 경우 예외 처리
        return null;
    }

    private InvalidExceptionType findInvalidExceptionType(InvalidException exception) {
        List<InvalidExceptionType> invalidExceptionTypes = Arrays.stream(
                InvalidExceptionType.values())
            .collect(Collectors.toList());
        for (InvalidExceptionType invalidExceptionType : invalidExceptionTypes) {
            if (exception.getClass() == invalidExceptionType.getException()) {
                return invalidExceptionType;
            }
        }
        return null;
    }


    //    TODO : 해당 메서드 지우기
    private String getMessage(Exception ex) {
        return ex.getMessage();
    }

    //    TODO : 중복 로직 제거하기
    private List<ExceptionType> createExceptionTypes() {
        List<ExceptionType> exceptionTypes = new ArrayList<>();
        exceptionTypes.addAll(
            Arrays.stream(AuthExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(EntryExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(MemberExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(ShareExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(WishExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(NotificationExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(KeywordExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(ChatRoomExceptionType.values()).collect(Collectors.toList()));
        return exceptionTypes;
    }
}
