package louie.hanse.shareplate.common.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.exception.GlobalException;
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
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@RequiredArgsConstructor
public class SocketExceptionHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
        Throwable ex) {
        ExceptionType exceptionType = findExceptionType(getMessage(ex));
        if (exceptionType != null) {
            GlobalException globalException = new GlobalException(exceptionType);
            GlobalExceptionResponse globalExceptionResponse = new GlobalExceptionResponse(
                globalException);
            byte[] payload;
            try {
                payload = objectMapper.writeValueAsBytes(globalExceptionResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
            return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private ExceptionType findExceptionType(String message) {
        List<ExceptionType> exceptionTypes = createExceptionTypes();
        for (ExceptionType exceptionType : exceptionTypes) {
            if (message.contains(exceptionType.getMessage())) {
                return exceptionType;
            }
        }
        return null;
    }

    private String getMessage(Throwable ex) {
        return ex.getMessage();
    }

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
