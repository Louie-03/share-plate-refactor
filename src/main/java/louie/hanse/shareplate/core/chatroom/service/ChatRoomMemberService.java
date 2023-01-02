package louie.hanse.shareplate.core.chatroom.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.core.chatroom.repository.ChatRoomMemberRepository;
import louie.hanse.shareplate.core.entry.repository.EntryRepository;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomType;
import louie.hanse.shareplate.core.chatroom.dto.response.ChatRoomMemberListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final EntryRepository entryRepository;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void exitChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);
        Member member = memberService.findByIdOrElseThrow(memberId);

        boolean isExistChatRoomMember = chatRoomMemberRepository
            .existsByMemberIdAndChatRoomId(memberId, chatRoomId);
        if (!isExistChatRoomMember) {
            throw new GlobalException(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED);
        }

        ChatRoomMember chatRoomMember = chatRoomMemberRepository
            .findWithShareByChatRoomIdAndMemberId(chatRoomId, memberId);
        Share share = chatRoomMember.getChatRoom().getShare();
        if (share.isLeftLessThanAnHour() && chatRoom.getType().equals(ChatRoomType.ENTRY)) {
            throw new GlobalException(ChatRoomExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME);
        }
        share.isWriterAndIsNotCancelThrowException(member);
        chatRoomMemberRepository.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);

        if (share.isNotEnd()) {
            entryRepository.deleteByMemberIdAndShareId(memberId, share.getId());
        }
    }

    public List<ChatRoomMemberListResponse> getChatRoomMemberList(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        return chatRoomMemberRepository
            .findAllByMemberId(memberId).stream()
            .map(ChatRoomMemberListResponse::new)
            .collect(Collectors.toList());
    }
}
