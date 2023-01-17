package louie.hanse.shareplate.core.keyword.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.domain.Latitude;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.common.domain.Longitude;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private KeywordContents contents;
    private Location location;
    private Latitude latitude;
    private Longitude longitude;

    public Keyword(Member member, KeywordContents contents, Location location,
        Latitude latitude, Longitude longitude) {
        this.member = member;
        this.contents = contents;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void isNotMemberThrowException(Member member) {
        if (!this.member.equals(member)) {
            throw new GlobalException(KeywordExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD);
        }
    }

    public String getContents() {
        return contents.getContents();
    }

    public String getLocation() {
        return location.getLocation();
    }

    public double getLatitude() {
        return latitude.getLatitude();
    }

    public double getLongitude() {
        return longitude.getLongitude();
    }
}
