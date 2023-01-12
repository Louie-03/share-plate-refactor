package louie.hanse.shareplate.core.share.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import louie.hanse.shareplate.core.share.domain.Share;

@Getter
public class ShareSearchResponse {

    private Long id;
    private String thumbnailUrl;
    private String title;
    private String location;
    private double latitude;
    private double longitude;
    private int price;
    private int originalPrice;
    private int currentRecruitment;
    private int finalRecruitment;
    private long writerId;
    private LocalDateTime createdDateTime;
    private LocalDateTime closedDateTime;

    public ShareSearchResponse(Share share) {
        this.id = share.getId();
        this.thumbnailUrl = share.getShareImages().get(0).getImageUrl();
        this.title = share.getTitle();
        this.location = share.getLocation();
        this.latitude = share.getLatitude();
        this.longitude = share.getLongitude();
        this.price = share.getPrice();
        this.originalPrice = share.getOriginalPrice();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.finalRecruitment = share.getRecruitment();
        this.writerId = share.getWriter().getId();
        this.createdDateTime = share.getCreatedDateTime();
        this.closedDateTime = share.getClosedDateTime();
    }
}
