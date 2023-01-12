package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.domain.ImageUrl;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ShareImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    private ImageUrl imageUrl;

    public ShareImage(Share share, String imageUrl) {
        this.share = share;
        this.imageUrl = new ImageUrl(imageUrl);
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }
}
