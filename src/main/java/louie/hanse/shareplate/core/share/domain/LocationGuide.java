package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.share.exception.InvalidLocationGuideException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LocationGuide {

    private String locationGuide;

    public LocationGuide(String locationGuide) {
        validateLocationGuide(locationGuide);
        this.locationGuide = locationGuide;
    }

    private void validateLocationGuide(String locationGuide) {
        if (StringUtils.isBlank(locationGuide)) {
            throw new InvalidLocationGuideException();
        }
    }
}
