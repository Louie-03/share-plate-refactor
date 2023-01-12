package louie.hanse.shareplate.core.chat.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chatroom.service.ChatRoomService;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.chat.domain.Chat;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;
import louie.hanse.shareplate.core.entry.domain.Entry;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.chat.repository.ChatRepository;
import louie.hanse.shareplate.core.entry.repository.EntryRepository;
import louie.hanse.shareplate.core.notification.repository.NotificationRepository;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.domain.NotificationType;
import louie.hanse.shareplate.core.chat.dto.response.ChatDetailResponse;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ChatRepository chatRepository;
    private final EntryRepository entryRepository;
    private final NotificationRepository notificationRepository;
    private final MessageSendingOperations messageSendingOperations;

    @Transactional
    public void saveAndResponseChatMessage(Long chatRoomId, Long memberId, String contents) {
        //TODO 추후 커스텀 예외처리 적용
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = chatRoom.getShare();
        if (chatRoom.isQuestion() && chatRoom.getChats().isEmpty()) {
            Member shareWriter = share.getWriter();
            chatRoom.addChatRoomMember(shareWriter);
            ActivityNotification activityNotification = new ActivityNotification(
                share, member, NotificationType.ACTIVITY, ActivityType.QUESTION_CHATROOM, member);
            notificationRepository.save(activityNotification);

            Entry entry = entryRepository.findByShareIdAndMemberId(share.getId(),
                shareWriter.getId());
            messageSendingOperations.convertAndSend(
                "/queue/notifications/entries/" + entry.getId(),
                new ActivityNotificationResponse(activityNotification));
        }
        Chat chat = new Chat(chatRoom, member, contents);
        chatRepository.save(chat);
        for (ChatRoomMember chatRoomMember : chatRoom.getChatRoomMembers()) {
            ChatDetailResponse chatDetailResponse = new ChatDetailResponse(
                chat, chatRoomMember.getMember(), share);
            messageSendingOperations.convertAndSend(
                "/topic/chatroom-members/" + chatRoomMember.getId(), chatDetailResponse);

        }
    }

    public int getTotalUnread(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        return chatRepository.getTotalUnread(memberId);
    }
}
