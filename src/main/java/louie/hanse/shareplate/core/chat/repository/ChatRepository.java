package louie.hanse.shareplate.core.chat.repository;

import java.util.Optional;
import louie.hanse.shareplate.core.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, CustomChatRepository {

    Optional<Chat> findTopByChatRoomIdOrderByWrittenDateTimeDesc(Long chatRoomId);
}
