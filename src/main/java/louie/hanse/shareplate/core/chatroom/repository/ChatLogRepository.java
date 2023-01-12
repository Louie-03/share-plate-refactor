package louie.hanse.shareplate.core.chatroom.repository;

import java.util.Optional;
import louie.hanse.shareplate.core.chatroom.domain.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    Optional<ChatLog> findByMemberIdAndChatRoomId(Long memberId, Long chatRoomId);
}

