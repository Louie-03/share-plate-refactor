package louie.hanse.shareplate.core.keyword.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import louie.hanse.shareplate.core.keyword.domain.Keyword;

@Getter
public class KeywordDetailResponse {
    private Long id;
    private String contents;

    @QueryProjection
    public KeywordDetailResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.contents = keyword.getContents();
    }
}
