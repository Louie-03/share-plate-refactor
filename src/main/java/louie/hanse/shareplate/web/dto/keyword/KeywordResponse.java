package louie.hanse.shareplate.web.dto.keyword;

import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;

@Getter
public class KeywordResponse {

    private Long id;
    private String keyword;
    private String location;

    public KeywordResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.keyword = keyword.getContents();
        this.location = keyword.getLocation();
    }
}