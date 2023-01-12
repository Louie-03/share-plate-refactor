package louie.hanse.shareplate.core.member.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.member.exception.InvalidNicknameException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {

    private String nickname;

    public Nickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new InvalidNicknameException();
        }
    }
}
