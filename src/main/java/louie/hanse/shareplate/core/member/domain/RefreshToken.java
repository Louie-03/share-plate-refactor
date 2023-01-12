package louie.hanse.shareplate.core.member.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.member.exception.InvalidRefreshTokenException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RefreshToken {

    private String refreshToken;

    public RefreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        this.refreshToken = refreshToken;
    }

    private void validateRefreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }
    }
}
