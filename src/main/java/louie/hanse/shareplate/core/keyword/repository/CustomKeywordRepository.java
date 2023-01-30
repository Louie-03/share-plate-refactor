package louie.hanse.shareplate.core.keyword.repository;

import java.util.List;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.core.keyword.domain.KeywordContents;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;

public interface CustomKeywordRepository {

    List<KeywordListResponse> getKeywords(Long memberId);

    boolean existsByMemberIdAndContentsAndLocation(Long memberId, KeywordContents contents,
        Location location);

    boolean existsByMemberIdAndLocation(Long memberId, Location location);

}
