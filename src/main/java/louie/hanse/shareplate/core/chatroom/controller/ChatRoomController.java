package louie.hanse.shareplate.core.chatroom.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.argumentresolver.MemberVerification;
import louie.hanse.shareplate.core.chatroom.dto.request.ChatRoomListRequest;
import louie.hanse.shareplate.core.chatroom.dto.response.ChatRoomDetailResponse;
import louie.hanse.shareplate.core.chatroom.dto.response.ChatRoomListResponse;
import louie.hanse.shareplate.core.chatroom.service.ChatRoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@Validated
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{id}")
    public ChatRoomDetailResponse chatRoomDetail(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 ChatroomId가 비어있습니다.")
        @Positive(message = "채팅방 id는 양수여야 합니다.") Long id,
        @MemberVerification Long memberId) {
        return chatRoomService.getDetail(id, memberId);
    }

    @GetMapping
    public List<ChatRoomListResponse> chatRoomList(@Valid ChatRoomListRequest chatRoomListRequest,
        @MemberVerification Long memberId) {
        return chatRoomService.getList(chatRoomListRequest, memberId);
    }

    @PostMapping
    public Map<String, Long> createQuestionChatRoom(
        @RequestBody Map<String, @Valid @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.") @Positive(message = "쉐어 id는 양수여야 합니다.") Long> map,
        @MemberVerification Long memberId) {
        Long shareId = map.get("shareId");
        Long id = chatRoomService.createQuestionChatRoom(memberId, shareId);
        return Collections.singletonMap("id", id);
    }

}
