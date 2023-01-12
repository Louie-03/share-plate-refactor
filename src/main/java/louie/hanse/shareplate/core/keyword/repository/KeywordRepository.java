package louie.hanse.shareplate.core.keyword.repository;

import java.util.List;
import java.util.Optional;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeywordRepository extends JpaRepository<Keyword, Long>,
    CustomKeywordRepository {

    @Query("select k from Keyword k "
        + "join k.member m on m.id <> :memberId "
        + "where :title like concat('%', k.contents, '%') and "
        + "(6371 * acos( cos( radians(:latitude) ) * cos( radians( k.latitude ) ) * "
        + "cos( radians( k.longitude ) - radians(:longitude) ) + "
        + "sin( radians(:latitude) ) * sin( radians( k.latitude ) ) ) ) <= 2")
    List<Keyword> findAllByContainsContentsAndNotMemberIdAndAroundShare(
        @Param("memberId") Long memberId, @Param("title") String title,
        @Param("longitude") double longitude, @Param("latitude") double latitude);

    @Modifying(clearAutomatically = true)
    @Query("delete from Keyword k where "
        + "k.member.id = :memberId and "
        + "k.location.location = :location")
    void deleteAllByMemberIdAndLocation(@Param("memberId") Long memberId,
        @Param("location") String location);

    @Query("select k from Keyword k where "
        + "k.member.id = :memberId and "
        + "k.location.location = :location")
    List<Keyword> findAllByMemberIdAndLocation(@Param("memberId") Long memberId,
        @Param("location") String location);

    @Query("select k from Keyword k join fetch k.member where k.id = :id")
    Optional<Keyword> findWithMemberById(@Param("id") Long id);

}
