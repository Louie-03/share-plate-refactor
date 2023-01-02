package louie.hanse.shareplate.core.share.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.core.share.domain.MineType;
import louie.hanse.shareplate.core.share.domain.ShareType;

@Getter
@Setter
public class ShareMineSearchRequest {

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private MineType mineType;
    private ShareType shareType;
    private boolean isExpired;
}
