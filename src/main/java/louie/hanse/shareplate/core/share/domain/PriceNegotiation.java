package louie.hanse.shareplate.core.share.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.share.exception.InvalidPriceNegotiationException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PriceNegotiation {

    private boolean priceNegotiation;

    public PriceNegotiation(Boolean priceNegotiation) {
        validatePriceNegotiation(priceNegotiation);
        this.priceNegotiation = priceNegotiation;
    }

    private void validatePriceNegotiation(Boolean priceNegotiation) {
        if (Objects.isNull(priceNegotiation)) {
            throw new InvalidPriceNegotiationException();
        }
    }
}
