package louie.hanse.shareplate.core.share.dto.request;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.common.validator.share.ValidShareLatitude;
import louie.hanse.shareplate.common.validator.share.ValidShareLongitude;

@Setter
@Getter
public class ShareRecommendationRequest {

    private String keyword;

    @ValidShareLatitude
    private Double latitude;

    @ValidShareLongitude
    private Double longitude;
}
