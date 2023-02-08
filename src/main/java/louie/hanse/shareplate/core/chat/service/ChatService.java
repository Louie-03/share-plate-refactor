package louie.hanse.shareplate.core.chat.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chat.domain.Chat;
import louie.hanse.shareplate.core.chat.event.ChatSaveEvent;
import louie.hanse.shareplate.core.chat.repository.ChatRepository;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.service.ChatRoomService;
import louie.hanse.shareplate.core.entry.domain.Entry;
import louie.hanse.shareplate.core.entry.repository.EntryRepository;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.domain.NotificationType;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationSaveEvent;
import louie.hanse.shareplate.core.notification.repository.NotificationRepository;
import louie.hanse.shareplate.core.share.domain.Share;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void save(Long chatRoomId, Long memberId, String contents) {
        //TODO 추후 커스텀 예외처리 적용
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);
        Member member = memberService.findByIdOrElseThrow(memberId);

        // TODO : 해당 로직 메서드로 분리
        if (chatRoom.isQuestion() && chatRoom.getChats().isEmpty()) {
            Share share = chatRoom.getShare();
            Member shareWriter = share.getWriter();
            chatRoom.addChatRoomMember(shareWriter);
            ActivityNotification activityNotification = new ActivityNotification(
                share, member, NotificationType.ACTIVITY, ActivityType.QUESTION_CHATROOM, member);
            notificationRepository.save(activityNotification);

            Entry entry = entryRepository.findByShareIdAndMemberId(share.getId(),
                shareWriter.getId());
            eventPublisher.publishEvent(new ActivityNotificationSaveEvent(
                activityNotification.getId(), entry.getId()));
        }

        Chat chat = new Chat(chatRoom, member, contents);
        chatRepository.save(chat);

        eventPublisher.publishEvent(new ChatSaveEvent(chat.getId()));
    }

    public int getTotalUnread(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        return chatRepository.getTotalUnread(memberId);
    }
}
