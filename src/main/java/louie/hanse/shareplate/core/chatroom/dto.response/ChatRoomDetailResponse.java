package louie.hanse.shareplate.core.chatroom.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomType;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.chat.dto.response.ChatDetailResponse;
import louie.hanse.shareplate.core.share.dto.response.SharePreviewResponse;

@Getter
public class ChatRoomDetailResponse {

    private Long chatRoomMemberId;
    private ChatRoomType type;
    private SharePreviewResponse share;
    private List<ChatDetailResponse> chats;

    public ChatRoomDetailResponse(ChatRoomMember chatRoomMember, Member member) {
        ChatRoom chatRoom = chatRoomMember.getChatRoom();
        this.chatRoomMemberId = chatRoomMember.getId();
        this.type = chatRoom.getType();
        this.share = new SharePreviewResponse(chatRoom.getShare());
        this.chats = chatRoom.getChats().stream()
            .map(chat -> new ChatDetailResponse(chat, member, chatRoom.getShare()))
            .collect(Collectors.toList());
    }
}

