package louie.hanse.shareplate.core.share.repository;

import static com.querydsl.core.types.dsl.Expressions.constant;
import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.querydsl.core.types.dsl.MathExpressions.sin;
import static louie.hanse.shareplate.core.share.domain.QShare.share;
import static louie.hanse.shareplate.core.wish.domain.QWish.wish;
import static louie.hanse.shareplate.core.entry.domain.QEntry.entry;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.share.domain.ShareType;
import louie.hanse.shareplate.core.share.dto.request.ShareRecommendationRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareSearchRequest;
import louie.hanse.shareplate.core.share.dto.response.QShareRecommendationResponse;
import louie.hanse.shareplate.core.share.dto.response.ShareRecommendationResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class CustomShareRepositoryImpl implements CustomShareRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Share> searchAroundMember(ShareSearchRequest request) {
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        return queryFactory
            .selectFrom(share)
            .where(
                typeEq(request.getType()),
                titleContains(request.getKeyword()),
                share.cancel.eq(false),
                share.closedDateTime.closedDateTime.gt(LocalDateTime.now()),
                acos(cos(radians(constant(latitude)))
                    .multiply(cos(radians(share.latitude.latitude)))
                    .multiply(cos(radians(share.longitude.longitude).subtract(radians(constant(longitude)))))
                    .add(sin(radians(constant(latitude))).multiply(sin(radians(share.latitude.latitude)))))
                    .multiply(constant(6371))
                    .loe(2)
            ).fetch();
    }

    @Override
    public List<ShareRecommendationResponse> recommendationAroundMember(
        ShareRecommendationRequest request) {
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        return queryFactory.select(new QShareRecommendationResponse(share))
            .from(share)
            .where(
                titleContains(request.getKeyword()),
                share.cancel.eq(false),
                share.closedDateTime.closedDateTime.gt(LocalDateTime.now()),
                acos(cos(radians(constant(latitude)))
                    .multiply(cos(radians(share.latitude.latitude)))
                    .multiply(cos(radians(share.longitude.longitude).subtract(radians(constant(longitude)))))
                    .add(sin(radians(constant(latitude))).multiply(sin(radians(share.latitude.latitude)))))
                    .multiply(constant(6371))
                    .loe(2)
            )
            .fetch();
    }

    @Override
    public List<Share> findByWriterIdAndTypeAndIsExpired(
        Long writerId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .where(
                typeEq(type),
                share.writer.id.eq(writerId),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    @Override
    public List<Share> findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .join(share.entries, entry).on(entry.member.id.eq(memberId))
            .where(
                share.writer.id.ne(memberId),
                typeEq(type),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    @Override
    public List<Share> findWithWishByMemberIdAndTypeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .join(share.wishList, wish).on(wish.member.id.eq(memberId))
            .where(
                typeEq(type),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    private BooleanExpression typeEq(ShareType type) {
        return ObjectUtils.isEmpty(type) ? null : share.type.eq(type);
    }

    private BooleanExpression isExpired(boolean expired, LocalDateTime currentDateTime) {
        if (expired) {
            return share.closedDateTime.closedDateTime.lt(currentDateTime);
        }
        return share.closedDateTime.closedDateTime.gt(currentDateTime);
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? share.title.title.contains(keyword) : null;
    }

}
