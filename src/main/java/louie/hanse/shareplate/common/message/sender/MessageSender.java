package louie.hanse.shareplate.common.message.sender;

import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chat.domain.Chat;
import louie.hanse.shareplate.core.chat.dto.response.ChatDetailResponse;
import louie.hanse.shareplate.core.chat.repository.ChatRepository;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.notification.domain.Notification;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MessageSender {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    public void sendActivityNotifications(List<Long> activityNotificationIds, List<Long> entryIds) {

        for (int i = 0; i < activityNotificationIds.size(); i++) {
            ActivityNotification activityNotification = notificationService
                .findActivityNotificationByIdOrElseThrow(activityNotificationIds.get(i));

            String destination = "/queue/notifications/entries/" + entryIds.get(i);

            simpMessagingTemplate.convertAndSend(destination,
                new ActivityNotificationResponse(activityNotification));
        }
    }

    @Transactional(readOnly = true)
    public void sendKeywordNotifications(List<Long> keywordNotificationIds, List<Long> keywordIds) {

        for (int i = 0; i < keywordNotificationIds.size(); i++) {
            Notification notification = notificationService
                .findKeywordNotificationByIdOrElseThrow(keywordNotificationIds.get(i));

            String destination = "/queue/notifications/keywords/" + keywordIds.get(i);

            simpMessagingTemplate.convertAndSend(destination,
                new KeywordNotificationResponse(notification));
        }
    }

    @Transactional(readOnly = true)
    public void sendChatDetail(Long chatId) {
        Chat chat = chatRepository.findWithShareWriterAndChatRoomMemberAndMember(chatId);
        ChatRoom chatRoom = chat.getChatRoom();

        for (ChatRoomMember chatRoomMember : chatRoom.getChatRoomMembers()) {
            ChatDetailResponse chatDetailResponse = new ChatDetailResponse(
                chat, chatRoomMember.getMember(), chatRoom.getShare());

            String destination = "/topic/chatroom-members/" + chatRoomMember.getId();

            simpMessagingTemplate.convertAndSend(destination, chatDetailResponse);
        }
    }

}
