package louie.hanse.shareplate.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.converter.StringToChatRoomTypeConverter;
import louie.hanse.shareplate.converter.StringToMineTypeConverter;
import louie.hanse.shareplate.converter.StringToShareTypeConverter;
import louie.hanse.shareplate.interceptor.LoginVerificationInterceptor;
import louie.hanse.shareplate.interceptor.LogoutInterceptor;
import louie.hanse.shareplate.interceptor.MemberVerificationInterceptor;
import louie.hanse.shareplate.interceptor.ReissueAccessTokenInterceptor;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.LoginService;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    public static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberVerificationInterceptor(jwtProvider))
            .order(1)
            .addPathPatterns("/members", "/members/location", "/shares", "/shares/mine",
                "/shares/{id}", "/shares/{id}/entry", "/entries", "/wish-list", "/chatrooms/{id}",
                "/chat-logs/update-read-time", "/chats/unread", "/chatroom-members", "/chatrooms",
                "/keywords", "/keywords/{id}", "/keywords/location", "/notifications/activity",
                "/notifications/keyword", "/notifications/{id}", "/notifications")
            .excludePathPatterns("/shares/recommendation", "/shares/writer");

        registry.addInterceptor(new LogoutInterceptor(jwtProvider, loginService))
            .order(1)
            .addPathPatterns("/logout");

        registry.addInterceptor(new ReissueAccessTokenInterceptor(jwtProvider, loginService))
            .order(1)
            .addPathPatterns("/reissue/access-token");

        registry.addInterceptor(new LoginVerificationInterceptor(jwtProvider, loginService))
            .order(1)
            .addPathPatterns("/login/verification");
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializers(
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)))
            .deserializers(
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToShareTypeConverter());
        registry.addConverter(new StringToMineTypeConverter());
        registry.addConverter(new StringToChatRoomTypeConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .exposedHeaders("*")
            .allowedHeaders("*");
    }
}
