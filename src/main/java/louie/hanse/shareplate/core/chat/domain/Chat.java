package louie.hanse.shareplate.core.chat.domain;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
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
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Embedded
    private ChatContents contents;

    private LocalDateTime writtenDateTime = LocalDateTime.now();

    public Chat(ChatRoom chatRoom, Member writer, String contents) {
        chatRoom.shareIsCancelThrowException();
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.contents = new ChatContents(contents);
    }

    public String getContents() {
        return contents.getContents();
    }

}
