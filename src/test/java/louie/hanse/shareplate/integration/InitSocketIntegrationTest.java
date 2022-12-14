package louie.hanse.shareplate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;


public class InitSocketIntegrationTest extends InitIntegrationTest {

    protected StompSession stompSession;
    protected CompletableFuture<Object> completableFuture = new CompletableFuture<>();

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));

        String accessToken = jwtProvider.createAccessToken(2370842997L);
        StompHeaders stompHeaders = createStompHeaders(accessToken);
        stompSession = stompClient.connect("ws://localhost:" + port + "/websocket",
                (WebSocketHttpHeaders) null, stompHeaders,
                getStompSessionHandlerAdapter(String.class))
            .get(3, TimeUnit.SECONDS);
    }

    protected static StompHeaders createStompHeaders(String accessToken) {
        return createStompHeaders(accessToken, null);
    }

    protected static StompHeaders createStompHeaders(String accessToken, String destination) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add(HttpHeaders.AUTHORIZATION, accessToken);
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }

    protected <T> StompSessionHandlerAdapter getStompSessionHandlerAdapter(Class<T> clazz) {
        return new StompSessionHandlerAdapter() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    completableFuture.complete(objectMapper.readValue((byte[]) payload, clazz));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void handleException(StompSession session, @Nullable StompCommand command,
                StompHeaders headers, byte[] payload, Throwable exception) {
                throw new RuntimeException(exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
