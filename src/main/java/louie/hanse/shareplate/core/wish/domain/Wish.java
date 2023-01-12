package louie.hanse.shareplate.core.wish.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.share.domain.Share;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Wish(Share share, Member member) {
        this.share = share;
        share.getWishList().add(this);
        this.member = member;
    }
}
