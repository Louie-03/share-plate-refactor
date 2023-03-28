package louie.hanse.shareplate.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.argumentresolver.MemberVerificationArgumentResolver;
import louie.hanse.shareplate.common.converter.StringToChatRoomTypeConverter;
import louie.hanse.shareplate.common.converter.StringToMineTypeConverter;
import louie.hanse.shareplate.common.converter.StringToShareTypeConverter;
import louie.hanse.shareplate.common.interceptor.LoginVerificationInterceptor;
import louie.hanse.shareplate.common.interceptor.LogoutInterceptor;
import louie.hanse.shareplate.common.interceptor.ReissueAccessTokenInterceptor;
import louie.hanse.shareplate.common.jwt.JwtProvider;
import louie.hanse.shareplate.core.member.service.LoginService;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberVerificationArgumentResolver(jwtProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

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
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT));
        registrar.registerFormatters(registry);

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
            .allowedHeaders("*")
            .maxAge(0);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
