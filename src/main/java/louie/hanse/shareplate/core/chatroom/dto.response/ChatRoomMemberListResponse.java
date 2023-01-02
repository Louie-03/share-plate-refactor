package louie.hanse.shareplate.core.chatroom.dto.response;

import lombok.Getter;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;

@Getter
public class ChatRoomMemberListResponse {
    private Long id;
    private Long chatRoomId;

    public ChatRoomMemberListResponse(ChatRoomMember chatRoomMember) {
        this.id = chatRoomMember.getId();
        this.chatRoomId = chatRoomMember.getChatRoom().getId();
    }
}
