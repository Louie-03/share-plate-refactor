package louie.hanse.shareplate.core.keyword.repository;

import static louie.hanse.shareplate.core.keyword.domain.QKeyword.keyword;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.keyword.dto.response.KeywordListResponse;

@RequiredArgsConstructor
public class CustomKeywordRepositoryImpl implements
    louie.hanse.shareplate.core.keyword.repository.CustomKeywordRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<KeywordListResponse> getKeywords(Long memberId) {
        List<String> locations = queryFactory
            .select(keyword.location).distinct()
            .from(keyword)
            .where(keyword.member.id.eq(memberId))
            .fetch();

        List<KeywordListResponse> keywordListResponses = new ArrayList<>();
        for (String location : locations) {
            List<Keyword> keywords = queryFactory
                .selectFrom(keyword)
                .where(
                    keyword.location.eq(location),
                    keyword.member.id.eq(memberId)
                )
                .fetch();
            keywordListResponses.add(new KeywordListResponse(location, keywords));
        }

        return keywordListResponses;
    }
}
