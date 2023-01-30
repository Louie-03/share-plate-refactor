package louie.hanse.shareplate.core.keyword.repository;

import static java.util.Objects.isNull;
import static louie.hanse.shareplate.core.keyword.domain.QKeyword.keyword;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.keyword.domain.KeywordContents;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;

@RequiredArgsConstructor
public class CustomKeywordRepositoryImpl implements CustomKeywordRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<KeywordListResponse> getKeywords(Long memberId) {
        List<Location> locations = queryFactory
            .select(keyword.location).distinct()
            .from(keyword)
            .where(keyword.member.id.eq(memberId))
            .fetch();

        List<KeywordListResponse> keywordListResponses = new ArrayList<>();
        for (Location location : locations) {
            List<Keyword> keywords = queryFactory
                .selectFrom(keyword)
                .where(
                    keyword.location.eq(location),
                    keyword.member.id.eq(memberId)
                )
                .fetch();
            keywordListResponses.add(new KeywordListResponse(location.getLocation(), keywords));
        }

        return keywordListResponses;
    }

    @Override
    public boolean existsByMemberIdAndContentsAndLocation(Long memberId, KeywordContents contents,
        Location location) {
        Integer result = queryFactory
            .selectOne()
            .from(keyword)
            .where(
                keyword.member.id.eq(memberId),
                keyword.contents.eq(contents),
                keyword.location.eq(location)
            ).fetchFirst();

        return !isNull(result);
    }

    @Override
    public boolean existsByMemberIdAndLocation(Long memberId, Location location) {
        Integer result = queryFactory
            .selectOne()
            .from(keyword)
            .where(
                keyword.member.id.eq(memberId),
                keyword.location.eq(location)
            ).fetchFirst();

        return !isNull(result);
    }
}
