package louie.hanse.shareplate.core.chatroom.controller;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.argumentresolver.MemberVerification;
import louie.hanse.shareplate.core.chatroom.dto.response.ChatRoomMemberListResponse;
import louie.hanse.shareplate.core.chatroom.service.ChatRoomMemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chatroom-members")
@RestController
@Validated
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @GetMapping
    public List<ChatRoomMemberListResponse> getChatRoomMemberList(@MemberVerification Long memberId) {
        return chatRoomMemberService.getChatRoomMemberList(memberId);
    }

    @DeleteMapping
    public void exitChatRoom(
        @RequestBody Map<String, @Valid @NotNull(message = "요청한 채팅방정보 필드값이 비어있습니다.")
        @Positive(message = "채팅방 id는 양수여야 합니다.") Long> map, @MemberVerification Long memberId) {
        Long chatRoomId = map.get("chatRoomId");
        chatRoomMemberService.exitChatRoom(chatRoomId, memberId);
    }
}
