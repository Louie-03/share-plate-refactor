package louie.hanse.shareplate.core.share.repository;

import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.share.domain.ShareType;
import louie.hanse.shareplate.core.share.dto.response.ShareRecommendationResponse;
import louie.hanse.shareplate.core.share.dto.request.ShareRecommendationRequest;
import louie.hanse.shareplate.core.share.dto.request.ShareSearchRequest;

public interface CustomShareRepository {

    List<Share> searchAroundMember(ShareSearchRequest request);

    List<ShareRecommendationResponse> recommendationAroundMember(ShareRecommendationRequest request);

    List<Share> findByWriterIdAndTypeAndIsExpired(
        Long writerId, ShareType type, boolean expired, LocalDateTime currentDateTime);

    List<Share> findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime);

    List<Share> findWithWishByMemberIdAndTypeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime);

}

