package louie.hanse.shareplate.core.keyword.dto.response;

import lombok.Getter;
import louie.hanse.shareplate.core.keyword.domain.Keyword;

@Getter
public class KeywordRegisterResponse {

    private Long id;
    private String contents;
    private String location;
    private double latitude;
    private double longitude;

    public KeywordRegisterResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.contents = keyword.getContents();
        this.location = keyword.getLocation();
        this.latitude = keyword.getLatitude();
        this.longitude = keyword.getLongitude();
    }
}
