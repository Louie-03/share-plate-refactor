package louie.hanse.shareplate.core.share.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Setter;
import louie.hanse.shareplate.common.validator.share.ValidShareImage;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.share.domain.ShareType;
import org.springframework.web.multipart.MultipartFile;

@Setter
public class ShareEditRequest {

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private ShareType type;

    @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.")
    @Size(max = 5, message = "이미지 5개를 초과하였습니다.")
    private List<@Valid @NotNull @ValidShareImage MultipartFile> images;

    private String title;
    private Integer price;
    private Integer originalPrice;
    private Integer recruitment;
    private String location;
    private String locationGuide;
    private Boolean locationNegotiation;
    private Boolean priceNegotiation;
    private List<String> hashtags;
    private Double latitude;
    private Double longitude;
    private String description;
    private LocalDateTime closedDateTime;

    public Share toEntity(Long id, Member member) {
        return new Share(id, member, type, title, price, originalPrice, recruitment, location,
            latitude, longitude, description, closedDateTime, locationGuide, locationNegotiation,
            priceNegotiation);
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public List<String> getHashtags() {
        return hashtags;
    }
}
