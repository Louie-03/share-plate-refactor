package louie.hanse.shareplate.common.message.event.listener;

import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chat.domain.Chat;
import louie.hanse.shareplate.core.chat.dto.response.ChatDetailResponse;
import louie.hanse.shareplate.core.chat.event.ChatSaveEvent;
import louie.hanse.shareplate.core.chat.repository.ChatRepository;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoom;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomMember;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.notification.domain.Notification;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.event.keyword.KeywordNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MessageSenderEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    @KafkaListener(topics = "activity-notifications-save")
    public void sendActivityNotifications(ActivityNotificationsSaveEvent event) {
        List<Long> activityNotificationIds = event.getActivityNotificationIds();

        for (int i = 0; i < activityNotificationIds.size(); i++) {
            ActivityNotification activityNotification = notificationService
                .findActivityNotificationByIdOrElseThrow(activityNotificationIds.get(i));

            String destination = "/queue/notifications/entries/" + event.getEntryIds().get(i);

            simpMessagingTemplate.convertAndSend(destination,
                new ActivityNotificationResponse(activityNotification));
        }
    }

    @Transactional(readOnly = true)
    @KafkaListener(topics = "keyword-notifications-save")
    public void sendKeywordNotifications(KeywordNotificationsSaveEvent event) {
        List<Long> keywordNotificationIds = event.getKeywordNotificationIds();

        for (int i = 0; i < keywordNotificationIds.size(); i++) {
            Notification notification = notificationService
                .findKeywordNotificationByIdOrElseThrow(keywordNotificationIds.get(i));

            String destination = "/queue/notifications/keywords/" + event.getKeywordIds().get(i);

            simpMessagingTemplate.convertAndSend(destination,
                new KeywordNotificationResponse(notification));
        }
    }

    @Transactional(readOnly = true)
    @KafkaListener(topics = "chat-save")
    public void sendChatDetail(ChatSaveEvent event) {
        Chat chat = chatRepository.findWithShareWriterAndChatRoomMemberAndMember(event.getChatId());
        ChatRoom chatRoom = chat.getChatRoom();

        for (ChatRoomMember chatRoomMember : chatRoom.getChatRoomMembers()) {
            ChatDetailResponse chatDetailResponse = new ChatDetailResponse(
                chat, chatRoomMember.getMember(), chatRoom.getShare());

            String destination = "/topic/chatroom-members/" + chatRoomMember.getId();

            simpMessagingTemplate.convertAndSend(destination, chatDetailResponse);
        }
    }

}
