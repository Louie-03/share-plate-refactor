package louie.hanse.shareplate.core.share.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.domain.Latitude;
import louie.hanse.shareplate.common.domain.Location;
import louie.hanse.shareplate.common.domain.Longitude;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.wish.domain.Wish;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.entry.domain.Entry;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.common.exception.type.EntryExceptionType;
import louie.hanse.shareplate.common.exception.type.ShareExceptionType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareImage> shareImages = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShareType type;

    private boolean cancel;
    private Title title;
    private Price price;
    private OriginalPrice originalPrice;
    private Recruitment recruitment;
    private LocationNegotiation locationNegotiation;
    private PriceNegotiation priceNegotiation;
    private LocationGuide locationGuide;
    private Location location;
    private Latitude latitude;
    private Longitude longitude;
    private Description description;
    private ClosedDateTime closedDateTime;
    private LocalDateTime createdDateTime = LocalDateTime.now();

    public Share(Long id, Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime closedDateTime, String locationGuide, boolean locationNegotiation,
        boolean priceNegotiation) {
        this.id = id;
        this.writer = writer;
        this.type = type;
        this.title = new Title(title);
        this.price = new Price(price);
        this.originalPrice = new OriginalPrice(originalPrice);
        this.recruitment = new Recruitment(recruitment);
        this.locationNegotiation = new LocationNegotiation(locationNegotiation);
        this.priceNegotiation = new PriceNegotiation(priceNegotiation);
        this.locationGuide = new LocationGuide(locationGuide);
        this.location = new Location(location);
        this.latitude = new Latitude(latitude);
        this.longitude = new Longitude(longitude);
        this.description = new Description(description);
        this.closedDateTime = new ClosedDateTime(closedDateTime);
    }

    public Share(Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime closedDateTime, String locationGuide, boolean locationNegotiation,
        boolean priceNegotiation) {
        this(null, writer, type, title, price, originalPrice, recruitment, location, latitude, longitude,
            description, closedDateTime, locationGuide, locationNegotiation, priceNegotiation);
    }

    public void addShareImage(String shareImageUrl) {
        ShareImage shareImage = new ShareImage(this, shareImageUrl);
        shareImages.add(shareImage);
    }

    public void addHashtag(String contents) {
        hashtags.add(new Hashtag(this, contents));
    }

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
    }

    public boolean isNotEnd() {
        return closedDateTime.isNotEnd();
    }

    public boolean isEnd() {
        return !isNotEnd();
    }

    public boolean isLeftLessThanAnHour() {
        return closedDateTime.isLeftLessThanAnHour();
    }


    public void isNotWriterThrowException(Member member) {
        if (isNotWriter(member)) {
            throw new GlobalException(ShareExceptionType.IS_NOT_WRITER);
        }
    }

    public boolean isWriter(Member member) {
        return !isNotWriter(member);
    }

    public int getCurrentRecruitment() {
        return entries.size();
    }

    public int getWishCount() {
        return wishList.size();
    }

    public ChatRoom getEntryChatRoom() {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.isEntry()) {
                return chatRoom;
            }
        }
        //TODO 추후 커스텀 예외 처리
        throw new RuntimeException("참여 채팅방을 찾을 수 없습니다.");
    }

    private boolean isNotWriter(Member member) {
        return !writer.equals(member);
    }

    public void recruitmentQuotaExceededThrowException() {
        if (getCurrentRecruitment() >= getRecruitment()) {
            throw new GlobalException(EntryExceptionType.SHARE_OVERCAPACITY);
        }
    }

    public void cancel() {
        cancel = true;
    }

    public void isClosedThrowException() {
        if (closedDateTime.isClosed()) {
            throw new GlobalException(ShareExceptionType.SHARE_IS_CLOSED);
        }
    }

    public void isCanceledThrowException() {
        if (cancel) {
            throw new GlobalException(ShareExceptionType.SHARE_IS_CANCELED);
        }
    }

    public boolean isNotCancel() {
        return !cancel;
    }

    public void isWriterAndIsNotCancelThrowException(Member member) {
        if (isWriter(member) && isNotCancel()) {
            throw new GlobalException(ChatRoomExceptionType.SHARE_WRITER_CANNOT_LEAVE);
        }
    }

    public boolean isCancel() {
        return cancel;
    }

    public String getTitle() {
        return title.getTitle();
    }

    public int getPrice() {
        return price.getPrice();
    }

    public int getOriginalPrice() {
        return originalPrice.getOriginalPrice();
    }

    public int getRecruitment() {
        return recruitment.getRecruitment();
    }

    public boolean isLocationNegotiation() {
        return locationNegotiation.isLocationNegotiation();
    }

    public boolean isPriceNegotiation() {
        return priceNegotiation.isPriceNegotiation();
    }

    public String getLocationGuide() {
        return locationGuide.getLocationGuide();
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

    public String getDescription() {
        return description.getDescription();
    }

    public LocalDateTime getClosedDateTime() {
        return closedDateTime.getClosedDateTime();
    }


}
