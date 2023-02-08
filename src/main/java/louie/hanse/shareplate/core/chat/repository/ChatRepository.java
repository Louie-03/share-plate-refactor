package louie.hanse.shareplate.core.chat.repository;

import java.util.Optional;
import louie.hanse.shareplate.core.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long>, CustomChatRepository {

    Optional<Chat> findTopByChatRoomIdOrderByWrittenDateTimeDesc(Long chatRoomId);

    @Query("select c from Chat c "
        + "join fetch c.chatRoom cr "
        + "join fetch cr.share s "
        + "join fetch s.writer shareWriter "
        + "join fetch cr.chatRoomMembers crm "
        + "join fetch crm.member chatRommMemberInMember "
        + "where c.id = :id")
    Chat findWithShareWriterAndChatRoomMemberAndMember(@Param("id") Long id);
}
