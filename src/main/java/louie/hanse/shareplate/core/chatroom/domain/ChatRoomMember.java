package louie.hanse.shareplate.core.chatroom.domain;

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
import louie.hanse.shareplate.core.member.domain.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public ChatRoomMember(Member member, ChatRoom chatRoom) {
        this.member = member;
        this.chatRoom = chatRoom;
    }
}
