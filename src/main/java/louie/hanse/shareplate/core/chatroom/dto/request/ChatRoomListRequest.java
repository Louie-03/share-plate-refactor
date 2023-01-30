package louie.hanse.shareplate.core.chatroom.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomType;

@Setter
@Getter
public class ChatRoomListRequest {

    @NotNull(message = "요청한 채팅방정보 필드값이 비어있습니다.")
    private ChatRoomType type;

}
