package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.NumberUtils;
import louie.hanse.shareplate.core.share.exception.InvalidPriceException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {

    private int price;

    public Price(Integer price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(Integer price) {
        if (NumberUtils.isNullOrNotPositive(price)) {
            throw new InvalidPriceException();
        }
    }
}
