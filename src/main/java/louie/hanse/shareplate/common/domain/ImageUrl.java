package louie.hanse.shareplate.common.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.exception.invalid.InvalidImageUrlException;
import louie.hanse.shareplate.common.utils.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ImageUrl {

    private String imageUrl;

    public ImageUrl(String imageUrl) {
        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validateImageUrl(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            throw new InvalidImageUrlException();
        }
    }
}
