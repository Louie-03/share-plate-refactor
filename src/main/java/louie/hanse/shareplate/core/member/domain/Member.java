package louie.hanse.shareplate.core.member.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import louie.hanse.shareplate.common.domain.ImageUrl;
import louie.hanse.shareplate.core.share.domain.Share;

@Getter
@Table(name = "members")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private Long id;

    @OneToMany(mappedBy = "writer")
    private List<Share> shares = new ArrayList<>();

    private Nickname nickname;
    private Email email;
    private RefreshToken refreshToken;

    @AttributeOverride(name = "imageUrl", column = @Column(name = "profile_image_url"))
    private ImageUrl profileImageUrl;

    @AttributeOverride(name = "imageUrl", column = @Column(name = "thumbnail_image_url"))
    private ImageUrl thumbnailImageUrl;

    public Member(Long id, String profileImageUrl, String thumbnailImageUrl, String nickname,
        String email) {
        this.id = id;
        this.profileImageUrl = new ImageUrl(profileImageUrl);
        this.thumbnailImageUrl = new ImageUrl(thumbnailImageUrl);
        this.nickname = new Nickname(nickname);
        this.email = new Email(email);
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = new ImageUrl(profileImageUrl);
    }

    public void changeThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = new ImageUrl(thumbnailImageUrl);
    }

    public void changeNickname(String nickname) {
        this.nickname = new Nickname(nickname);
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = new RefreshToken(refreshToken);
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(profileImageUrl,
            member.profileImageUrl) && Objects.equals(thumbnailImageUrl,
            member.thumbnailImageUrl) && Objects.equals(nickname, member.nickname)
            && Objects.equals(email, member.email) && Objects.equals(refreshToken,
            member.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profileImageUrl, thumbnailImageUrl, nickname, email, refreshToken);
    }

    public String getProfileImageUrl() {
        return profileImageUrl.getImageUrl();
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl.getImageUrl();
    }

    public String getNickname() {
        return nickname.getNickname();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getRefreshToken() {
        return refreshToken.getRefreshToken();
    }
}

