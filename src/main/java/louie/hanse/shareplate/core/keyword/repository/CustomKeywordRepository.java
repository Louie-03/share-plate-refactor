package louie.hanse.shareplate.core.keyword.repository;

import java.util.List;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;

public interface CustomKeywordRepository {

    List<KeywordListResponse> getKeywords(Long memberId);

    boolean existsByMemberIdAndContentsAndLocation(Long memberId, String contents, String location);

    boolean existsByMemberIdAndLocation(Long memberId, String location);

}
