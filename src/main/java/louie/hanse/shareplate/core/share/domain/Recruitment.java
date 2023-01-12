package louie.hanse.shareplate.core.share.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.share.exception.InvalidRecruitmentException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Recruitment {

    public static final int MIN_RECRUITMENT = 2;

    private int recruitment;

    public Recruitment(Integer recruitment) {
        validateRecruitment(recruitment);
        this.recruitment = recruitment;
    }

    private void validateRecruitment(Integer recruitment) {
        if (Objects.isNull(recruitment) || recruitment < MIN_RECRUITMENT) {
            throw new InvalidRecruitmentException();
        }
    }
}
