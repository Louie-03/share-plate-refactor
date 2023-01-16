package louie.hanse.shareplate.core.keyword.dto.request;

import lombok.Getter;
import louie.hanse.shareplate.common.validator.keyword.ValidKeywordLatitude;
import louie.hanse.shareplate.common.validator.keyword.ValidKeywordLongitude;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
public class KeywordRegisterRequest {

    private String contents;

    private String location;

    @ValidKeywordLatitude
    private Double latitude;

    @ValidKeywordLongitude
    private Double longitude;

    public Keyword toEntity(Member member) {
        return new Keyword(member, contents, location, latitude, longitude);
    }
}
