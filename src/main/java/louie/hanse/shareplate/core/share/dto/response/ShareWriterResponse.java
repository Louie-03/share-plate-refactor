package louie.hanse.shareplate.core.share.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
public class ShareWriterResponse {

    private String writer;
    private String thumbnailUrl;
    private int shareCount;
    private List<ShareRecommendationResponse> shares;

    public ShareWriterResponse(Member writer) {
        List<Share> shares = writer.getShares();
        this.writer = writer.getNickname();
        this.thumbnailUrl = writer.getThumbnailImageUrl();
        this.shareCount = shares.size();
        this.shares = shares.stream()
            .map(ShareRecommendationResponse::new)
            .collect(Collectors.toList());
    }
}
