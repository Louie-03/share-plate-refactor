package louie.hanse.shareplate.core.share.dto.request;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.core.share.domain.ShareType;
import louie.hanse.shareplate.common.validator.share.ValidShareLatitude;
import louie.hanse.shareplate.common.validator.share.ValidShareLongitude;

@Setter
@Getter
public class ShareSearchRequest {

    private ShareType type;
    private String keyword;

    @ValidShareLatitude
    private Double latitude;
    @ValidShareLongitude
    private Double longitude;
}
