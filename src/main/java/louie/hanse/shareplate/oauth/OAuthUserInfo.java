package louie.hanse.shareplate.oauth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthUserInfo {

    private Long id;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public class Properties {

        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }

    @Getter
    public class KakaoAccount {

        private String email;
    }

    public Member toMember() {
        return new Member(id, properties.profileImage, properties.thumbnailImage,
            properties.nickname, kakaoAccount.email);
    }
}
