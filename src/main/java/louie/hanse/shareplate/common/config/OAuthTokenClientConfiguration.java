package louie.hanse.shareplate.common.config;

import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.AuthExceptionType;
import org.springframework.context.annotation.Bean;

public class OAuthTokenClientConfiguration {

    private final Default defaultErrorDecoder = new Default();

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            int status = response.status();
            if (isClientError(status)) {
                throw new GlobalException(AuthExceptionType.INCORRECT_AUTHORIZATION_CODE);
            }
            return defaultErrorDecoder.decode(methodKey, response);
        };
    }

    private boolean isClientError(int status) {
        return 400 <= status && status < 500;
    }
}
