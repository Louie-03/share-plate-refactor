package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.NumberUtils;
import louie.hanse.shareplate.core.share.exception.InvalidOriginalPriceException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OriginalPrice {

    private int originalPrice;

    public OriginalPrice(Integer originalPrice) {
        validateOriginalPrice(originalPrice);
        this.originalPrice = originalPrice;
    }

    private void validateOriginalPrice(Integer originalPrice) {
        if (NumberUtils.isNullOrNotPositive(originalPrice)) {
            throw new InvalidOriginalPriceException();
        }
    }
}
