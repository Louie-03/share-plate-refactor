package louie.hanse.shareplate.core.share.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import louie.hanse.shareplate.core.share.domain.Share;

@Getter
public class ShareRecommendationResponse {

    private Long id;
    private Long writerId;
    private String thumbnailUrl;
    private String title;
    private String location;
    private int price;
    private int currentRecruitment;
    private int finalRecruitment;
    private LocalDateTime createdDateTime;
    private LocalDateTime closedDateTime;

    @QueryProjection
    public ShareRecommendationResponse(Share share) {
        this.id = share.getId();
        this.writerId = share.getWriter().getId();
        this.thumbnailUrl = share.getShareImages().get(0).getImageUrl();
        this.title = share.getTitle();
        this.location = share.getLocation();
        this.price = share.getPrice();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.finalRecruitment = share.getRecruitment();
        this.createdDateTime = share.getCreatedDateTime();
        this.closedDateTime = share.getClosedDateTime();
    }
}
