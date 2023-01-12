package louie.hanse.shareplate.core.member.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.member.exception.InvalidEmailException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    private String email;

    public Email(String email) {
        validateEmail(email);
        this.email = email;
    }

    private void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidEmailException();
        }
    }
}
