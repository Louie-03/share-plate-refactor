package louie.hanse.shareplate.common.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.AuthExceptionType;
import louie.hanse.shareplate.common.jwt.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@RequiredArgsConstructor
public class MemberVerificationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String requestUrlPattern = (String) request.getAttribute(
            HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String method = request.getMethod();

        if (("/shares/{id}".equals(requestUrlPattern) || "/shares".equals(requestUrlPattern))
            && HttpMethod.GET.matches(method)) {
            return true;
        }

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

        Long memberId = jwtProvider.decodeMemberId(accessToken);
        request.setAttribute("memberId", memberId);

        return true;
    }
}
