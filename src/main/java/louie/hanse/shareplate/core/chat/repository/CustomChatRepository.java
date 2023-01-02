package louie.hanse.shareplate.core.chat.repository;

public interface CustomChatRepository {

    int getTotalUnread(Long memberId);

    int getUnread(Long memberId, Long chatRoomId);
}
