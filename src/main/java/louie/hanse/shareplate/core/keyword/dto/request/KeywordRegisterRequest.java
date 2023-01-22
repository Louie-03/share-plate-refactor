package louie.hanse.shareplate.core.keyword.dto.request;

import lombok.Getter;
import louie.hanse.shareplate.common.domain.Latitude;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.common.domain.Longitude;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.keyword.domain.KeywordContents;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
public class KeywordRegisterRequest {

    private String contents;
    private String location;
    private Double latitude;
    private Double longitude;

    public Keyword toEntity(Member member) {
        return new Keyword(
            member,
            getContents(),
            getLocation(),
            new Latitude(latitude),
            new Longitude(longitude)
        );
    }

    public KeywordContents getContents() {
        return new KeywordContents(contents);
    }

    public Location getLocation() {
        return new Location(location);
    }
}
