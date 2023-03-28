package louie.hanse.shareplate.common.argumentresolver;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.AuthExceptionType;
import louie.hanse.shareplate.common.jwt.JwtProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class MemberVerificationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(MemberVerification.class);
        boolean hasParameterType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && hasParameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(accessToken)) {
            throw new GlobalException(AuthExceptionType.EMPTY_ACCESS_TOKEN);
        }

        try {
            jwtProvider.verifyAccessToken(accessToken);
        } catch (TokenExpiredException e) {
            throw new GlobalException(AuthExceptionType.EXPIRED_ACCESS_TOKEN);
        } catch (JWTVerificationException e) {
            throw new GlobalException(AuthExceptionType.TAMPERING_ACCESS_TOKEN);
        }

        return jwtProvider.decodeMemberId(accessToken);
    }
}
