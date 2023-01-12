package louie.hanse.shareplate.core.keyword.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.common.validator.keyword.ValidKeywordLatitude;
import louie.hanse.shareplate.common.validator.keyword.ValidKeywordLongitude;

@Getter
public class KeywordRegisterRequest {

    @NotBlank(message = "요청한 키워드정보 필드값이 비어있습니다.")
    private String contents;

    @NotBlank(message = "요청한 키워드정보 필드값이 비어있습니다.")
    private String location;

    @ValidKeywordLatitude
    private Double latitude;

    @ValidKeywordLongitude
    private Double longitude;

    public Keyword toEntity(Member member) {
        return new Keyword(member, contents, location, latitude, longitude);
    }
}
