package louie.hanse.shareplate.core.entry.repository;

import java.util.List;
import louie.hanse.shareplate.core.entry.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query("select e from Entry e where e.share.id = :shareId and e.member.id <> :memberId")
    List<Entry> findAllByShareIdAndNotMemberId(
        @Param("shareId") Long shareId, @Param("memberId") Long memberId);

    Entry findByShareIdAndMemberId(Long shareId, Long memberId);

    boolean existsByMemberIdAndShareId(Long memberId, Long shareId);

    void deleteByMemberIdAndShareId(Long memberId, Long shareId);

    @Query("select e from Entry e "
        + "join fetch e.member m "
        + "join fetch e.share s "
        + "join fetch s.shareImages si ")
    List<Entry> findAllByShareId(Long shareId);

    List<Entry> findAllByMemberId(Long memberId);
}
